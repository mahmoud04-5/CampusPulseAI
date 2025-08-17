package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.service.IClubRecommendationService;
import com.example.campuspulseai.service.ISurveyService;
import com.example.campuspulseai.southBound.entity.Club;
import com.example.campuspulseai.southBound.entity.QuestionChoices;
import com.example.campuspulseai.southBound.entity.User;
import com.example.campuspulseai.southBound.entity.SurveyUserAnswers;
import com.example.campuspulseai.southBound.repository.IClubRepository;
import com.example.campuspulseai.southBound.repository.IQuestionChoicesRepository;
import com.example.campuspulseai.southBound.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClubRecommendationServiceImpl implements IClubRecommendationService {
    private final IUserRepository userRepository;
    private final IClubRepository clubRepository;
    private final ISurveyService surveyService;
    private final AIService aiService;
    private final IQuestionChoicesRepository questionChoicesRepository;

    @Override
    public List<Club> getRecommendationsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (!surveyService.isSurveyCompleted()) {
            return List.of();
        }

        List<SurveyUserAnswers> surveyUserAnswers = user.getSurveyUserAnswers();
        if (surveyUserAnswers == null || surveyUserAnswers.isEmpty()) {
            return List.of();
        }

        String userInterests = extractInterestsFromSurvey(surveyUserAnswers);
        if (userInterests == null || userInterests.trim().isEmpty()) {
            userInterests = "general activities";
        }

        // Generate AI-based club recommendations directly
        String prompt = String.format(
                "Based on the user's selected options: %s, infer their interests and recommend 5 college clubs. " +
                        "Provide the club names as a comma-separated list.", userInterests
        );
        String aiResponse = aiService.chat(prompt); // Use AIService's chat method
        List<String> recommendedClubNames = Arrays.asList(aiResponse.split(",\\s*"));

        // Map AI recommendations to Club entities
        return recommendedClubNames.stream()
                .map(name -> clubRepository.findByClubNameIgnoreCase(name)
                        .orElse(null))
                .filter(club -> club != null)
                .limit(5)
                .collect(Collectors.toList());
    }

    public String extractInterestsFromSurvey(List<SurveyUserAnswers> surveyUserAnswers) {
        // Aggregate all question answers into a single map
        Map<String, Object> allAnswers = surveyUserAnswers.stream()
                .map(SurveyUserAnswers::getQuestionAnswers)
                .filter(answers -> answers != null)
                .flatMap(answers -> answers.entrySet().stream())
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue()
                ));

        // Extract interests from choice texts
        String interests = allAnswers.entrySet().stream()
                .filter(entry -> entry.getValue() instanceof List)
                .flatMap(entry -> ((List<?>) entry.getValue()).stream())
                .map(choiceId -> {
                    if (choiceId instanceof Long) {
                        QuestionChoices choice = questionChoicesRepository.findById((Long) choiceId)
                                .orElse(null);
                        return choice != null ? choice.getChoice() : null; // Use choice text
                    }
                    return null;
                })
                .filter(choiceText -> choiceText != null && !choiceText.trim().isEmpty())
                .distinct()
                .collect(Collectors.joining(", "));

        return interests.isEmpty() ? null : interests;
    }
}