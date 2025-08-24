package com.example.campuspulseai.northbound.controller;


import com.example.campuspulseai.domain.dto.request.CreateClubRequest;
import org.springframework.http.ResponseEntity;
import com.example.campuspulseai.domain.dto.response.CreateClubResponse;
import com.example.campuspulseai.domain.dto.response.GetClubResponse;
import com.example.campuspulseai.service.IClubRecommendationService;
import com.example.campuspulseai.service.IClubService;
import com.example.campuspulseai.southbound.entity.Club;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RestController
@Tag(name = "Club endpoints", description = "Endpoints for club operations")
@RequestMapping("/api/clubs")
public class ClubController {

    private final IClubService clubService;
    private final IClubRecommendationService clubRecommendationService;

    @Operation(summary = "Create a new club", description = "Creates a new club with the provided details.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateClubResponse createClub(@Valid @RequestBody CreateClubRequest createClubRequest) throws AccessDeniedException {
        return clubService.createClub(createClubRequest);
    }

    @Operation(summary = "Get club by id", description = "Retrieves an club by its ID.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    GetClubResponse getClubById(@PathVariable Long id) {
        return clubService.getClubById(id);
    }


    @Operation(summary = "Update club by ID", description = "Updates an existing club's information.")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    CreateClubResponse updateClub(@PathVariable Long id,@Valid @RequestBody CreateClubRequest updateClubRequest) throws AccessDeniedException {
        return clubService.updateClub(id, updateClubRequest);
    }


    @Operation(summary = "Delete club by ID", description = "Deletes a club by its ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteClubById(@PathVariable Long id) throws AccessDeniedException {
        clubService.deleteClubById(id);
    }

    @Operation(summary = "Get clubs / search clubs",
            description = "Retrieves a paginated list of clubs. Optionally filters by a search query.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<GetClubResponse> getClubs(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return clubService.getClubs(query, page, size);
    }

    @Operation(
            summary = "Get Club Recommendations",
            description = "Retrieves personalized club recommendations for a user based on their survey responses."
    )
    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<GetClubResponse>> getRecommendationsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(clubRecommendationService.getRecommendationsForUser(userId));
    }


}