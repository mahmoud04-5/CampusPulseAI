package com.example.campuspulseai.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SurveyQuestionDTO {
    private Long questionId;
    private String questionText;
    private List<String> choices;
    private List<Long> selectedChoicesIds;
}
