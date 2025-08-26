package com.example.campuspulseai.southbound.specification;

import com.example.campuspulseai.southbound.entity.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public interface IEventSpecifications {

    Specification<Event> hasId(Long id);

    Specification<Event> hasClubId(Long clubId);

    Specification<Event> isActive();

    Specification<Event> hasEventDate(LocalDateTime eventDateTime);

    Specification<Event> hasEventWithinGivenDays();
}
