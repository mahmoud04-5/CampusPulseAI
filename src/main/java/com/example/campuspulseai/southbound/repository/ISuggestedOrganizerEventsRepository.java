package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.SuggestedOrganizerEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISuggestedOrganizerEventsRepository extends JpaRepository<SuggestedOrganizerEvent, Long> {
}
