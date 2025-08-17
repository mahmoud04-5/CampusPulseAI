package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.SurveyUserAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ISurveyUserAnswersRepository extends JpaRepository<SurveyUserAnswers, Long> {
    Optional<SurveyUserAnswers> findByUserId(Long userId);

}
