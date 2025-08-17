package com.example.campuspulseai.domain.dto.request;

import com.example.campuspulseai.common.validation.ValidCategoryNullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class EditEventRequest {
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String title;

    @Size(min = 1, max = 200, message = "Description must be between 1 and 200 characters")
    private String description;

    @Future
    private LocalDateTime startTime;

    @Size(min = 1, max = 50, message = "Location must be between 1 and 50 characters")
    private String location;

    @Min(5)
    @Max(2000)
    private Integer capacity;

    @ValidCategoryNullable
    private String category;

    private String imageUrl;
}
