package com.example.campuspulseai.service;


import com.example.campuspulseai.domain.dto.response.GetClubResponse;

import com.example.campuspulseai.southbound.entity.SurveyUserAnswers;

import java.util.List;

public interface IClubRecommendationService {
    public List<GetClubResponse> getRecommendationsForUser(Long userId);

    public String extractInterestsFromSurvey(List<SurveyUserAnswers> surveyUserAnswers);

}
