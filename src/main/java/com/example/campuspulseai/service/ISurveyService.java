package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.DTO.SurveyQuestionDTO;

import java.util.List;

public interface ISurveyService {
    List<SurveyQuestionDTO> getAllSurveyQuestions();

    void submitSurveyResponse(List<SurveyQuestionDTO> surveyResponses);
}
