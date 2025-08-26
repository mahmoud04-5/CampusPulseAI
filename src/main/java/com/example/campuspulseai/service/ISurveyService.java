package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.dto.SurveyQuestionDTO;

import java.util.List;

public interface ISurveyService {
    List<SurveyQuestionDTO> getAllSurveyQuestions();
    List<SurveyQuestionDTO> getUserResponses(Long userId);
    void submitSurveyResponse(List<SurveyQuestionDTO> surveyResponses);


}
