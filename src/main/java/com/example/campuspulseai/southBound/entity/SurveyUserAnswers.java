package com.example.campuspulseai.southBound.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;


@Entity
@Table(name = "survey_user_answers")
@Data
public class SurveyUserAnswers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;


    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "questions_answers", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> questionAnswers;


}
