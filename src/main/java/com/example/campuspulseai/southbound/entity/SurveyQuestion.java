package com.example.campuspulseai.southbound.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "survey_questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String question;



    @Column(name = "allow_mulltiple_answers", nullable = false)
    private Boolean allowMultipleAnswers = false;

    @OneToMany(mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)

    private List<QuestionChoices> choices;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedAt;
}
