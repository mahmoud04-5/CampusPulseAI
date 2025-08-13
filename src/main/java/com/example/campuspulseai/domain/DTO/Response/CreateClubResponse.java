package com.example.campuspulseai.domain.DTO.Response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CreateClubResponse {
    private long clubId;
    private String name;
    private String description;
    private String logoUrl;
    private boolean isActive;
    private List<GetEventResponse> events;
}
