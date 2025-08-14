package com.example.campuspulseai.domain.dto.Request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateClubRequest {
    private String clubName;
    private String clubDescription;
    private String logoUrl;

}

