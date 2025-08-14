package com.example.campuspulseai.service;

import com.example.campuspulseai.domain.dto.Request.CreateClubRequest;
import com.example.campuspulseai.domain.dto.Response.CreateClubResponse;
import com.example.campuspulseai.domain.dto.Response.GetClubResponse;
import org.springframework.data.domain.Page;


public interface IClubService {

    // Creates a new club and assigns the organizer.
    CreateClubResponse createClub(CreateClubRequest createClubRequest);

    // Retrieves a single club by its ID, including all its public-facing details and events.
    GetClubResponse getClubById(Long id);

    // Updates an existing club (edit information). This method should be for the organizer CreateClubResponse updateClub(CreateClubResponse updateClubRequest);

    // Deletes a club.This method should be for the organizer
    void deleteClubById(Long id);

    // This method retrieves a paginated list of clubs, optionally filtered by a search query.
    Page<GetClubResponse> getClubs(String query, int page, int size);
}
