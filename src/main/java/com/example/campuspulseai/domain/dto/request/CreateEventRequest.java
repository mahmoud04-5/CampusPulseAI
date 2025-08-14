package com.example.campuspulseai.domain.dto.request;

import com.example.campuspulseai.domain.validation.ValidCategory;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class CreateEventRequest {
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String title;

    @Size(min = 1, max = 200, message = "Description must be between 1 and 200 characters")
    private String description;

    @Future
    @NotNull
    private LocalDateTime startTime;

    @Size(min = 1, max = 50, message = "Location must be between 1 and 50 characters")
    private String location;

    @Min(5)
    @Max(2000)
    private Integer capacity;

    @ValidCategory
    @NotNull(message = "Event category is required")
    private String category;


    private String imageUrl;
}
