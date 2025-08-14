package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.service.IClubRecommendationService;
import com.example.campuspulseai.service.ISurveyService;
import com.example.campuspulseai.southbound.entity.Club;
import com.example.campuspulseai.southbound.entity.QuestionChoices;
import com.example.campuspulseai.southbound.entity.SurveyUserAnswers;
import com.example.campuspulseai.southbound.entity.User;
import com.example.campuspulseai.southbound.repository.IClubRepository;
import com.example.campuspulseai.southbound.repository.ISurveyUserAnswersRepository;
import com.example.campuspulseai.southbound.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClubRecommendationServiceImpl implements IClubRecommendationService {
    private final IUserRepository userRepository;
    private final IClubRepository clubRepository;
    private final ISurveyUserAnswersRepository surveyUserAnswersRepository;
    private final ISurveyService surveyService;


    @Override
    public List<Club> getRecommendationsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if (!surveyService.isSurveyCompleted()) {
            return List.of(); // No recommendations
        }


        List<SurveyUserAnswers> surveyUserAnswers = null;


        List<String> preferredCategories = surveyUserAnswers.stream()
                .map(SurveyUserAnswers::getChoice)
                .filter(choice -> choice != null && choice.getCategory() != null)
                .map(QuestionChoices::getCategory)
                .distinct()
                .toList();


        List<Club> recommendedClubs = null;//clubRepository.findByClubCategoryIn(preferredCategories);
        return recommendedClubs.stream()
                .limit(5)
                .collect(Collectors.toList());

    }
}
