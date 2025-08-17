package com.example.campuspulseai.southbound.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Table(name = "suggested_user_events")
@Entity
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class SuggestedUserEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp createdAt;
}
