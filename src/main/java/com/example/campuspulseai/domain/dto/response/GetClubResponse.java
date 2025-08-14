package com.example.campuspulseai.domain.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class GetClubResponse {

    // Core Club Information
    private Long clubId;
    private String name;
    private String description;
    private String logoUrl;
    private Boolean isActive;
    private OrganizerResponse organizerResponse;
    private List<GetEventResponse> events;
}
