package com.example.campuspulseai.domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

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
}
