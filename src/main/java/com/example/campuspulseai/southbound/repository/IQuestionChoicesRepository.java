package com.example.campuspulseai.southBound.repository;

import com.example.campuspulseai.southBound.entity.QuestionChoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionChoicesRepository extends JpaRepository<QuestionChoices, Long> {

}
