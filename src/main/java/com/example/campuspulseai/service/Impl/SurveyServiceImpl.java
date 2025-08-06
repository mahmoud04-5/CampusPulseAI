package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.domain.DTO.SurveyQuestionDTO;
import com.example.campuspulseai.service.ISurveyService;
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
