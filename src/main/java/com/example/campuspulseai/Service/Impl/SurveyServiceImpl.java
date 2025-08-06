package com.example.campuspulseai.Service.Impl;

import com.example.campuspulseai.Service.ISurveyService;
import com.example.campuspulseai.domain.DTO.SurveyQuestionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyServiceImpl implements ISurveyService {
    @Override
    public List<SurveyQuestionDTO> getAllSurveyQuestions() {
        return List.of();
    }

    @Override
    public void submitSurveyResponse(List<SurveyQuestionDTO> surveyResponses) {

    }
}
