package com.example.campuspulseai.southbound.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "event")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Changed to Long for consistency with nullable contexts

    @ManyToOne(fetch = FetchType.LAZY) // Changed to LAZY for performance
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 255)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedAt;

    @Column(name = "start_date")
    private LocalDateTime startTime;

    @OneToMany(mappedBy = "event")
    private List<EventAttendees> attendees = new ArrayList<>();

    @Column(name = "totalattendees", nullable = false)
    private Integer totalAttendees = 0;

    // Custom getter for isActive to follow JavaBean convention
    public boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getStartDate() {
        if (startTime == null) {
            throw new IllegalStateException("Start date is not set for the event");
        }
        return startTime; // Return the full ZonedDateTime
    }

    public void setTimeDate(@Future @NotNull LocalDateTime startTime) {
    }

    public LocalDateTime getTimeDate() {
        return startTime;
    }
}