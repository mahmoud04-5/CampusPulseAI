package com.example.campuspulseai.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OrganizerResponse {
    private String organizerName;
    private Long organizerId;
}
