package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.domain.dto.response.GetClubResponse;
import com.example.campuspulseai.service.IClubRecommendationService;
import com.example.campuspulseai.southbound.entity.*;
import com.example.campuspulseai.southbound.mapper.ClubMapper;
import com.example.campuspulseai.southbound.mapper.UserClubMapper;
import com.example.campuspulseai.southbound.repository.IClubRepository;
import com.example.campuspulseai.southbound.repository.IQuestionChoicesRepository;
import com.example.campuspulseai.southbound.repository.IUserRecommendedClubRepository;
import com.example.campuspulseai.southbound.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClubRecommendationServiceImpl implements IClubRecommendationService {

    private static final int MAX_RECOMMENDED_CLUBS = 5;
    private static final String DEFAULT_INTERESTS = "general activities";
    private static final double SIMILARITY_THRESHOLD = 0.75; // For fuzzy matching

    private final IUserRepository userRepository;
    private final IClubRepository clubRepository;
    private final AIService aiService;
    private final IQuestionChoicesRepository questionChoicesRepository;
    private final IUserRecommendedClubRepository userRecommendedClubRepository;
    private final ClubMapper clubMapper;
    private final UserClubMapper userClubMapper;

    @Override
    public List<GetClubResponse> getRecommendationsForUser(Long userId) {
        User user = findUserByIdOrThrow(userId);

        List<SurveyUserAnswers> surveyUserAnswers = user.getSurveyUserAnswers();
        if (hasNoSurveyAnswers(surveyUserAnswers)) {
            return List.of();
        }

        String userInterests = resolveUserInterests(surveyUserAnswers);

        String aiResponse = askAIForClubRecommendations(userInterests);

        List<String> recommendedClubNames = parseAIResponseToClubNames(aiResponse);

        List<Club> clubs = findMatchingClubsInDatabase(recommendedClubNames);

        saveUserRecommendations(user, clubs);

        return mapClubsToDto(clubs);
    }

    // ---------- Helper methods ----------

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    private boolean hasNoSurveyAnswers(List<SurveyUserAnswers> surveyUserAnswers) {
        return surveyUserAnswers == null || surveyUserAnswers.isEmpty();
    }

    private String resolveUserInterests(List<SurveyUserAnswers> surveyUserAnswers) {
        String extracted = extractInterestsFromSurvey(surveyUserAnswers);
        return (extracted == null || extracted.trim().isEmpty()) ? DEFAULT_INTERESTS : extracted;
    }

    private String askAIForClubRecommendations(String userInterests) {
        List<String> allClubNames = clubRepository.findAll().stream()
                .map(Club::getClubName)
                .toList();
        String clubNamesList = String.join(", ", allClubNames);
        String prompt = String.format(
                "Based on the user's selected options: %s, recommend up to %d college clubs from the following list: %s. " +
                        "Provide ONLY the club names as a comma-separated list, ensuring names match exactly.",
                userInterests, MAX_RECOMMENDED_CLUBS, clubNamesList
        );
        return aiService.chat(prompt);
    }

    private List<String> parseAIResponseToClubNames(String aiResponse) {
        return Arrays.stream(aiResponse.split(","))
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .toList();
    }

    private List<Club> findMatchingClubsInDatabase(List<String> recommendedClubNames) {
        List<Club> allClubs = clubRepository.findAll();
        return recommendedClubNames.stream()
                .map(name -> findBestMatchingClub(name, allClubs))
                .filter(Objects::nonNull)
                .distinct()
                .limit(MAX_RECOMMENDED_CLUBS)
                .toList();
    }

    private Club findBestMatchingClub(String recommendedName, List<Club> allClubs) {
        return allClubs.stream()
                .filter(club -> club.getClubName() != null)
                .max((club1, club2) -> {
                    double sim1 = calculateLevenshteinSimilarity(recommendedName, club1.getClubName());
                    double sim2 = calculateLevenshteinSimilarity(recommendedName, club2.getClubName());
                    return Double.compare(sim1, sim2);
                })
                .filter(club -> calculateLevenshteinSimilarity(recommendedName, club.getClubName()) >= SIMILARITY_THRESHOLD)
                .orElse(null);
    }

    private double calculateLevenshteinSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0.0;
        }
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) {
            return 1.0;
        }
        int distance = calculateLevenshteinDistance(s1.toLowerCase(), s2.toLowerCase());
        return 1.0 - ((double) distance / maxLength);
    }

    private int calculateLevenshteinDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    private void saveUserRecommendations(User user, List<Club> clubs) {
        List<SuggestedUserClubs> savedRecs = clubs.stream()
                .map(club -> userClubMapper.toSuggestedUserClubs(user, club))
                .toList();

        userRecommendedClubRepository.saveAll(savedRecs);
    }

    private List<GetClubResponse> mapClubsToDto(List<Club> clubs) {
        return clubMapper.toGetClubResponse(clubs);
    }

    // ---------- Existing survey extraction logic ----------

    @Override
    public String extractInterestsFromSurvey(List<SurveyUserAnswers> surveyUserAnswers) {
        Map<String, Object> allAnswers = surveyUserAnswers.stream()
                .map(SurveyUserAnswers::getQuestionAnswers)
                .filter(Objects::nonNull)
                .flatMap(answers -> answers.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b
                ));

        String interests = allAnswers.values().stream()
                .flatMap(value -> {
                    if (value instanceof List<?>) {
                        return ((List<?>) value).stream().map(this::resolveChoiceText);
                    } else {
                        return java.util.stream.Stream.of(resolveChoiceText(value));
                    }
                })
                .filter(choiceText -> choiceText != null && !choiceText.trim().isEmpty())
                .distinct()
                .collect(Collectors.joining(", "));

        return interests.isEmpty() ? null : interests;
    }

    private String resolveChoiceText(Object choiceIdOrText) {
        return switch (choiceIdOrText) {
            case Long id -> questionChoicesRepository.findById(id)
                    .map(QuestionChoices::getChoice)
                    .orElse(null);
            case Integer intId -> questionChoicesRepository.findById(intId.longValue())
                    .map(QuestionChoices::getChoice)
                    .orElse(null);
            case null -> null;
            default -> choiceIdOrText.toString();
        };
    }
}