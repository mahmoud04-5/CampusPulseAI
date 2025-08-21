package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.dto.SurveyQuestionDTO;
import com.example.campuspulseai.southbound.entity.User;

import java.util.List;

public interface ISurveyService {


    void submitSurveyResponse(List<SurveyQuestionDTO> surveyResponses);


}
