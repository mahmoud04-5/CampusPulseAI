package com.example.campuspulseai.southBound.repository;

import com.example.campuspulseai.southBound.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTimeDateAfterAndLabel(ZonedDateTime dateTime, String label);
    List<Event> findByTimeDateAfter(ZonedDateTime dateTime);

}
