package com.example.campuspulseai.service;


import com.example.campuspulseai.southbound.entity.Club;
import com.example.campuspulseai.southbound.entity.SurveyUserAnswers;

import java.util.List;

public interface IClubRecommendationService {
    List<Club> getRecommendationsForUser(Long userId);

    public String extractInterestsFromSurvey(List<SurveyUserAnswers> surveyUserAnswers);


}
