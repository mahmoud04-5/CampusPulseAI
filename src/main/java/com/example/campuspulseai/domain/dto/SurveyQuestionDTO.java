package com.example.campuspulseai.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SurveyQuestionDTO {
    private Long questionId;
    private String questionText;
    private List<String> choices;
    private List<Long> selectedChoicesIds;

    public SurveyQuestionDTO(Long id, String questionText, List<Long> choiceIds) {
        this.questionId = id;
        this.questionText = questionText;
        this.choices = null;
        this.selectedChoicesIds = choiceIds;
    }
}
