package com.example.campuspulseai.southBound.repository;

import com.example.campuspulseai.southBound.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISurveyQuestionRepository extends JpaRepository<SurveyQuestion,Long> {
}
