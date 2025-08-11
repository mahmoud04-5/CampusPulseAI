package com.example.campuspulseai.domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OrganizerResponse {
    private String organizerName;
    private long organizerId;
}
