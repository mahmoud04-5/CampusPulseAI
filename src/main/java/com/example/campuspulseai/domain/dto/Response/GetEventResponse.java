package com.example.campuspulseai.domain.dto.Response;

import com.example.campuspulseai.southBound.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
public class GetEventResponse {
    private long id;
    private String title;
    private String description;
    private String location;
    private Timestamp startTime;
    private Integer capacity;
    private String imageUrl;
    private String eventCategory;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public GetEventResponse(long id, String title, Club club, String description, ZonedDateTime timeDate, String label) {
    }

    public GetEventResponse(long id, String title, String description, ZonedDateTime timeDate, String label) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = Timestamp.from(timeDate.toInstant());
        this.location = label;
    }

}
