package com.example.campuspulseai.southBound.entity;

import java.io.Serializable;

public class UserEventId implements Serializable {
    private Long userId;
    private Long eventId;

    public UserEventId() {
    }

    public UserEventId(Long userId, Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    // Getters and setters (required for JPA composite keys)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
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