package com.example.campuspulseai.domain.dto.response;

import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
