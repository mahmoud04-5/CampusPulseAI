package com.example.campuspulseai.service.Impl;

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
        String prompt = String.format(
                "Based on the user's selected options: %s, infer their interests and recommend %d college clubs. " +
                        "Provide ONLY the club names as a comma-separated list.",
                userInterests, MAX_RECOMMENDED_CLUBS
        );
        return aiService.chat(prompt);
    }

    private List<String> parseAIResponseToClubNames(String aiResponse) {
        return Arrays.stream(aiResponse.split(","))
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .map(String::toLowerCase)
                .toList();
    }

    private List<Club> findMatchingClubsInDatabase(List<String> recommendedClubNames) {
        return recommendedClubNames.stream()
                .map(name -> clubRepository.findByClubNameIgnoreCase(name).orElse(null))
                .filter(Objects::nonNull)
                .distinct()
                .limit(MAX_RECOMMENDED_CLUBS)
                .toList();
    }



    private void saveUserRecommendations(User user, List<Club> clubs) {
        List<SuggestedUserClubs> savedRecs = clubs.stream()
                .map(club -> userClubMapper.toSuggestedUserClubs(user, club))
                .toList();

        userRecommendedClubRepository.saveAll(savedRecs);
    }

    private List<GetClubResponse> mapClubsToDto(List<Club> clubs) {
        return clubMapper.toDtoList(clubs);
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
        if (choiceIdOrText instanceof Long id) {
            return questionChoicesRepository.findById(id)
                    .map(QuestionChoices::getChoice)
                    .orElse(null);
        } else if (choiceIdOrText instanceof Integer intId) {
            return questionChoicesRepository.findById(intId.longValue())
                    .map(QuestionChoices::getChoice)
                    .orElse(null);
        } else {
            return choiceIdOrText.toString();
        }
    }
}
