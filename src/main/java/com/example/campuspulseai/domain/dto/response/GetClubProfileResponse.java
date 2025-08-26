package com.example.campuspulseai.domain.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class GetClubProfileResponse {
    private Long id;
    private String name;
    private String logoUrl;
    private String description;
    private Boolean isActive;
    private List<GetEventResponse> events;
}