package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findByStartTimeAfterAndCategory(LocalDateTime startDate, String category);

    List<Event> findByStartTimeAfter(LocalDateTime filterDate);

    Optional<Event> findByIdAndIsActiveTrue(Long id);

    List<Event> findByClubId(Long clubId);


}
