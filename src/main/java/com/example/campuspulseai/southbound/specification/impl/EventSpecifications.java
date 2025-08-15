package com.example.campuspulseai.southbound.specification.impl;

import com.example.campuspulseai.southbound.entity.Event;
import com.example.campuspulseai.southbound.specification.IEventSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class EventSpecifications implements IEventSpecifications {
    @Override
    public Specification<Event> hasClubId(Long clubId) {
        return (root, query, cb) -> clubId == null
                ? null
                : cb.equal(root.get("club").get("id"), clubId);
    }

    @Override
    public Specification<Event> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    @Override
    public Specification<Event> hasEventDate(LocalDateTime eventDateTime) {
        return (root, query, cb) -> {
            if (eventDateTime == null) return null;

            LocalDate date = eventDateTime.toLocalDate();
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime startOfNextDay = date.plusDays(1).atStartOfDay();

            return cb.and(
                    cb.greaterThanOrEqualTo(root.get("timeDate"), startOfDay),
                    cb.lessThan(root.get("timeDate"), startOfNextDay)
            );
        };
    }
}
