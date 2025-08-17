package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.SuggestedUserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISuggestedUserEventsRepository extends JpaRepository<SuggestedUserEvent, Long> {
    List<SuggestedUserEvent> findAllByUserId(Long userId);
}
