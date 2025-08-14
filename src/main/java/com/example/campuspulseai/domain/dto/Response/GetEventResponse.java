package com.example.campuspulseai.domain.dto.Response;

import com.example.campuspulseai.southBound.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class GetEventResponse {
    private long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startTime;
    private Integer capacity;
    private String imageUrl;
    private String eventCategory;
    private Timestamp createdAt;
    private Timestamp updatedAt;


    public GetEventResponse(long id, String title, String description, LocalDateTime timeDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = timeDate;
    }


    public GetEventResponse(long id, String title, Club club, String description, LocalDateTime timeDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = timeDate;
        if (club != null) {
            this.location = club.getName();
            this.imageUrl = club.getLogoUrl();
            //this.eventCategory = club.getClubCategory();  No such club category
        }
    }

}
