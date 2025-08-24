package com.example.campuspulseai.domain.dto.request;

import com.example.campuspulseai.common.validation.ValidCloudinaryPath;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateClubRequest {
    @Size(min = 1, max = 30, message = "Club Name must be between 1 and 30 characters")
    private String clubName;

    @Size(min = 1, max = 50, message = "Club Description must be between 1 and 30 characters")
    private String clubDescription;


    @ValidCloudinaryPath
    private String logoUrl;

}

