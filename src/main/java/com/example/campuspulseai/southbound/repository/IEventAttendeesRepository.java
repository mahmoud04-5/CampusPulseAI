package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.EventAttendees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IEventAttendeesRepository extends JpaRepository<EventAttendees, Long> {
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    List<EventAttendees> findByUserId(Long userId);
    List<EventAttendees> findByEventId(Long eventId);
    Optional<EventAttendees> findByUserIdAndEventId(Long userId, Long eventId);}
