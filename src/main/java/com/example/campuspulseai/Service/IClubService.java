package com.example.campuspulseai.Service;

import com.example.campuspulseai.domain.DTO.Request.CreateClubRequest;
import com.example.campuspulseai.domain.DTO.Response.CreateClubResponse;
import com.example.campuspulseai.domain.DTO.Response.GetClubResponse;

import java.util.List;

public interface IClubService {

    // Creates a new club and assigns the organizer.
    CreateClubResponse createClub(CreateClubRequest createClubRequest);

    // Retrieves a single club by its ID, including all its public-facing details and events.
    GetClubResponse getClubById(Long id);

    // Retrieves a list of all active clubs.
    List<GetClubResponse> getAllClubs();

    // Updates an existing club (edit information). This method should be for the organizer CreateClubResponse updateClub(CreateClubResponse updateClubRequest);

    // Deletes a club.This method should be for the organizer
    void deleteClubById(Long id);

    // Search method for the User
    List<GetClubResponse> searchClubs(String query);
}
