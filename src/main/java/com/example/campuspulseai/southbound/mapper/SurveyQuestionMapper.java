package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.domain.dto.SurveyQuestionDTO;
import com.example.campuspulseai.southbound.entity.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface SurveyQuestionMapper {
    @Mapping(target = "selectedChoicesIds", ignore = true)
    SurveyQuestionDTO toDto(SurveyQuestion surveyQuestion);

    List<SurveyQuestionDTO> toSurveyQuestionDTOList(List<SurveyQuestion> surveyQuestions);
}
