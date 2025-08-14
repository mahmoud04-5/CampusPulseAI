package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTimeDateAfterAndCategory(LocalDateTime startDate, String category);

    Optional<Event> findByIdAndIsActiveTrue(Long id);

}
