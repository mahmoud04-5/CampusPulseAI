package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
}
