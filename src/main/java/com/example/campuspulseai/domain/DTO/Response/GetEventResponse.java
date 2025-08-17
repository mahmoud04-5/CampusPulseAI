package com.example.campuspulseai.domain.DTO.Response;

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
    private int capacity;
    private String imageUrl;
    private String eventCategory;
    private Timestamp createdAt;
    private Timestamp updatedAt;


    public GetEventResponse(long id, String title, String description, ZonedDateTime timeDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = Timestamp.from(timeDate.toInstant());
    }


    public GetEventResponse(long id, String title, Club club, String description, ZonedDateTime timeDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = Timestamp.from(timeDate.toInstant());
        if (club != null) {
            this.imageUrl = club.getLogoUrl();
            this.eventCategory = club.getClubCategory();
        }
    }
}
