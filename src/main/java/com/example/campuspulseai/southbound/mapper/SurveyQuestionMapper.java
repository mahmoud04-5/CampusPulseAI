package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.domain.dto.QuestionChoiceDTO;
import com.example.campuspulseai.domain.dto.SurveyQuestionDTO;
import com.example.campuspulseai.southbound.entity.QuestionChoices;
import com.example.campuspulseai.southbound.entity.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SurveyQuestionMapper {

    @Mapping(target = "questionId", source = "id")
    @Mapping(target = "questionText", source = "question")
    @Mapping(target = "choices", source = "choices")
    @Mapping(target = "selectedChoicesIds", ignore = true)
    SurveyQuestionDTO toDto(SurveyQuestion surveyQuestion);

    List<SurveyQuestionDTO> toSurveyQuestionDTOList(List<SurveyQuestion> surveyQuestions);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "choice", source = "choice")
    QuestionChoiceDTO toDto(QuestionChoices choice);

    List<QuestionChoiceDTO> toDtoList(List<QuestionChoices> choices);
}


