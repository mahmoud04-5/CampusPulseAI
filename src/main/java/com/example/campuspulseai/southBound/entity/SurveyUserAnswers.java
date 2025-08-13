package com.example.campuspulseai.southBound.entity;

import com.example.campuspulseai.domain.DTO.SurveyQuestionDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Entity
@Table(name = "survey_user_answers")
@Data

public class SurveyUserAnswers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private SurveyQuestion surveyQuestion;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "choice_id", referencedColumnName = "id")
    private QuestionChoices choice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    //private String categoryAnswer;
    private String categoryAnswer;

    public void setUser(User user) {
        this.user = user;

    }

    public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
        this.surveyQuestion =surveyQuestion;
    }
    public void setChoice(QuestionChoices choice) {
        this.choice = choice;
    }

    public long getId() {
        return id;
    }

    public SurveyQuestion getSurveyQuestion() {
        return surveyQuestion;
    }

    public User getUser() {
        return user;
    }

    public QuestionChoices getChoice() {
        return choice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
