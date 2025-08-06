package com.example.campuspulseai.southBand.Repository;

import com.example.campuspulseai.southBand.Entity.QuestionChoices;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISurveyRepository extends CrudRepository<QuestionChoices, Long> {
}
