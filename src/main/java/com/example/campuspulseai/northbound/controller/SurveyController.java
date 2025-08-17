package com.example.campuspulseai.northbound.controller;

import com.example.campuspulseai.domain.dto.SurveyQuestionDTO;
import com.example.campuspulseai.service.ISurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Survey", description = "Survey Management API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/surveys")
public class SurveyController {
    private final ISurveyService surveyService;

    @CrossOrigin(origins = "http://localhost:8080")
    @Operation(summary = "Get Survey", description = "Retrieves the survey questions for the user.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SurveyQuestionDTO> getSurvey() {
        return surveyService.getAllSurveyQuestions();
    }

    @Operation(summary = "Submit Survey", description = "Submits the user's responses to the survey.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/submit")
    public void submitSurvey(@RequestBody List<SurveyQuestionDTO> surveyResponses) {
        surveyService.submitSurveyResponse(surveyResponses);
    }

}
