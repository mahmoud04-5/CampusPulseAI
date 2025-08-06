package com.example.campuspulseai.domain.DTO.Response;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class GetClubResponse {

    // Core Club Information
    private long clubId;
    private String name;
    private String description;
    private String logoUrl;
    private boolean isActive;
    private OrganizerResponse organizerResponse;
    private List<GetEventResponse> events;
}
