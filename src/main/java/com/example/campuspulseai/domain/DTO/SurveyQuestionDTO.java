package com.example.campuspulseai.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SurveyQuestionDTO {
    private long questionId;
    private String questionText;
    private List<String> choices;
}
