package com.example.campuspulseai.domain.dto.response;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class UpdateClubResponse {
    private String name;
    private String description;
    private String logoUrl;
    private Boolean isActive;
    private LocalDateTime updatedAt;
}
