package com.example.campuspulseai.southbound.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserEventId implements Serializable {
    private Long userId;
    private Long eventId;


    public UserEventId(Long userId, Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEventId that = (UserEventId) o;
        return userId.equals(that.userId) && eventId.equals(that.eventId);
    }

    @Override
    public int hashCode() {
        return 31 * userId.hashCode() + eventId.hashCode();
    }
}