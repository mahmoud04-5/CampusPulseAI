package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.DTO.SurveyQuestionDTO;
import com.example.campuspulseai.southBound.entity.User;

import java.util.List;

public interface ISurveyService {


    void submitSurveyResponse(List<SurveyQuestionDTO> surveyResponses);

    boolean isSurveyCompleted();

    User getAuthenticatedUser();

    List<SurveyQuestionDTO> getAllSurveyQuestions();
}