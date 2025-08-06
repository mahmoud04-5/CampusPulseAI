package com.example.campuspulseai.NorthBound.Controller;

import com.example.campuspulseai.Service.ISurveyService;
import com.example.campuspulseai.domain.DTO.SurveyQuestionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Survey", description = "Survey Management API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController {
    private final ISurveyService surveyService;

    @Operation(summary = "Get Survey", description = "Retrieves the survey questions for the user.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SurveyQuestionDTO> getSurvey() {
        return surveyService.getAllSurveyQuestions();
    }

    @Operation(summary = "Submit Survey", description = "Submits the user's responses to the survey.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/submit")
    public void submitSurvey(List<SurveyQuestionDTO> surveyResponses) {
        surveyService.submitSurveyResponse(surveyResponses);
    }

}
