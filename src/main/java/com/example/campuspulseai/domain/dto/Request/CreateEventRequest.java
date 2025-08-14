package com.example.campuspulseai.domain.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class CreateEventRequest {
    private String title;
    private String description;
    private String location;
    private Timestamp startTime;
    private Integer capacity;
    private String imageUrl;
    private String eventCategory;
}
