package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.domain.DTO.Request.CreateClubRequest;
import com.example.campuspulseai.domain.DTO.Response.CreateClubResponse;
import com.example.campuspulseai.domain.DTO.Response.GetClubResponse;
import com.example.campuspulseai.service.IClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


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
    public void deleteClubById(Long id) {
    }

    @Override
    public Page<GetClubResponse> getClubs(String query, int page, int size) {
        return null;
    }
}
