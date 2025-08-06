package com.example.campuspulseai.domain.DTO.Request;

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
    private int capacity;
    private String imageUrl;
    private String eventCategory;
}
