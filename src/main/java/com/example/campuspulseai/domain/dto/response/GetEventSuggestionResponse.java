package com.example.campuspulseai.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetEventSuggestionResponse {
    private String title;
    private String description;
    private String category;
}
