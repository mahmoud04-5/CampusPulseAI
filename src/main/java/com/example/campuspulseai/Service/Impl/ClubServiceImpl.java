package com.example.campuspulseai.Service.Impl;

import com.example.campuspulseai.Service.IClubService;
import com.example.campuspulseai.domain.DTO.Request.CreateClubRequest;
import com.example.campuspulseai.domain.DTO.Response.CreateClubResponse;
import com.example.campuspulseai.domain.DTO.Response.GetClubResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
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
