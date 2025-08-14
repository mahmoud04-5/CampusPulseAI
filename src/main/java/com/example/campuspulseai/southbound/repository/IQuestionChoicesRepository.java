package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.QuestionChoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionChoicesRepository extends JpaRepository<QuestionChoices, Long> {

}
