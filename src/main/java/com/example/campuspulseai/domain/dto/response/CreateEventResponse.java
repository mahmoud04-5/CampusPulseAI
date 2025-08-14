package com.example.campuspulseai.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateEventResponse {
    private String title;
    private Long eventId;
}
