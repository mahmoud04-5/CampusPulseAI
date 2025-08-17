package com.example.campuspulseai.southBound.repository;

import com.example.campuspulseai.southBound.entity.SurveyUserAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ISurveyUserAnswersRepository extends JpaRepository<SurveyUserAnswers, Long> {
    Optional<SurveyUserAnswers> findByUserId(Long userId);

}
