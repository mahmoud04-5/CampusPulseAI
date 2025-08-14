package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.QuestionChoices;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionChoicesRepository extends CrudRepository<QuestionChoices, Long> {

}
