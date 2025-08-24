package com.example.campuspulseai.southbound.specification.impl;

import com.example.campuspulseai.southbound.entity.Event;
import com.example.campuspulseai.southbound.specification.IEventSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class EventSpecifications implements IEventSpecifications {
    private static final int DAYS = 90;
    private static final String TIME_DATE = "timeDate";

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
                    cb.greaterThanOrEqualTo(root.get(TIME_DATE), startOfDay),
                    cb.lessThan(root.get(TIME_DATE), startOfNextDay)
            );
        };
    }

    @Override
    public Specification<Event> hasEventWithinGivenDays() {
        LocalDateTime eventDateTime = LocalDateTime.now();
        return (root, query, cb) -> {

            LocalDate date = eventDateTime.toLocalDate();
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime startOfNextWeek = date.plusDays(DAYS).atStartOfDay();

            return cb.and(
                    cb.greaterThanOrEqualTo(root.get(TIME_DATE), startOfDay),
                    cb.lessThan(root.get(TIME_DATE), startOfNextWeek)
            );
        };
    }
}
