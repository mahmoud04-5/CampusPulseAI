package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.dto.SurveyQuestionDTO;
import com.example.campuspulseai.southBound.entity.User;

import java.util.List;

public interface ISurveyService {
    List<SurveyQuestionDTO> getAllSurveyQuestions();

    void submitSurveyResponse(List<SurveyQuestionDTO> surveyResponses);

    boolean isSurveyCompleted();

    User getAuthenticatedUser();
}
