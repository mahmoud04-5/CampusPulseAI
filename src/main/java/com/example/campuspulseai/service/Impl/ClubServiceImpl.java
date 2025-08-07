package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.domain.DTO.Request.CreateClubRequest;
import com.example.campuspulseai.domain.DTO.Response.CreateClubResponse;
import com.example.campuspulseai.domain.DTO.Response.GetClubResponse;
import com.example.campuspulseai.service.IClubService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ClubServiceImpl implements IClubService {
    @Override
    public CreateClubResponse createClub(CreateClubRequest createClubRequest) {
        return null;
    }

    @Override
    public GetClubResponse getClubById(Long id) {
        return null;
    }

    @Override
    public List<GetClubResponse> getAllClubs() {
        return List.of();
    }

    @Override
    public void deleteClubById(Long id) {

    }

    @Override
    public List<GetClubResponse> searchClubs(String query) {
        return List.of();
    }
}
