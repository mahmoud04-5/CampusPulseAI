package com.example.campuspulseai.Service;

import com.example.campuspulseai.domain.DTO.SurveyQuestionDTO;

import java.util.List;

public interface ISurveyService {
    List<SurveyQuestionDTO> getAllSurveyQuestions();

    void submitSurveyResponse(List<SurveyQuestionDTO> surveyResponses);
}
