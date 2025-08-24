package com.example.campuspulseai.domain.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CreateClubResponse {
    private Long clubId;
    private String name;
    private String description;
    private String logoUrl;
    private Boolean isActive;
}
