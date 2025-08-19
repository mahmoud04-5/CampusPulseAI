package com.example.campuspulseai.domain.dto.request;

import jakarta.validation.constraints.Pattern;
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


    @Pattern(
            regexp = "^https://res\\.cloudinary\\.com/dct8rg7di.*$",
            message = "Logo URL must be a valid Cloudinary URL"
    )
    private String logoUrl;

}

