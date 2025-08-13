package com.example.campuspulseai.service;


import com.example.campuspulseai.southBound.entity.Club;

import java.util.List;

public interface IClubRecommendationService {
    public List<Club> getRecommendationsForUser(Long userId);

}
