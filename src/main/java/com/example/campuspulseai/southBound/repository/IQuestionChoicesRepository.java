package com.example.campuspulseai.southBound.repository;

import com.example.campuspulseai.southBound.entity.QuestionChoices;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionChoicesRepository extends CrudRepository<QuestionChoices, Long> {

}
