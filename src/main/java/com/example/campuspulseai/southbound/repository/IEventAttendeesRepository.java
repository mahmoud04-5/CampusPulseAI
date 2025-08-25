package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.EventAttendees;
import com.example.campuspulseai.southbound.entity.UserEventId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEventAttendeesRepository extends JpaRepository<EventAttendees, UserEventId> {
    Optional<EventAttendees> findByUserIdAndEventId(Long userId, Long eventId);
}
