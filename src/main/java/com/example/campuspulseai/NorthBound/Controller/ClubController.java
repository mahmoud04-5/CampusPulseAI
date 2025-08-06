package com.example.campuspulseai.NorthBound.Controller;


import com.example.campuspulseai.Service.IClubService;
import com.example.campuspulseai.domain.DTO.Request.CreateClubRequest;
import com.example.campuspulseai.domain.DTO.Response.CreateClubResponse;
import com.example.campuspulseai.domain.DTO.Response.GetClubResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Data
@Tag(name = "Club endpoints", description = "Endpoints for club operations")

@RequestMapping("/api/clubs")
public class ClubController {

    @Autowired
    private final IClubService clubService;

    @Operation(summary = "Create a new club", description = "Creates a new club with the provided details.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateClubResponse createClub(CreateClubRequest createClubRequest) {
        return clubService.createClub(createClubRequest);
    }

    @Operation(summary = "Get club by id", description = "Retrieves an club by its ID.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    GetClubResponse getClubById(@PathVariable Long id) {
        return clubService.getClubById(id);
    }

    @Operation(summary = "Get all clubs", description = "Retrieves a list of all clubs")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    List<GetClubResponse> getAllClubs() {
        return clubService.getAllClubs();
    }

    @Operation(summary = "Delete club by ID", description = "Deletes a club by its ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteClubById(@PathVariable Long id) {
        clubService.deleteClubById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    List<GetClubResponse> searchClubs(@RequestParam("query") String query) {
        return clubService.searchClubs(query);
    }
}
