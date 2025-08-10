package com.example.campuspulseai.domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateEventResponse {
    private String title;
    private long eventId;
}
