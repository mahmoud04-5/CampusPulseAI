package com.example.campuspulseai.service;


import com.example.campuspulseai.southbound.entity.Club;

import java.util.List;

public interface IClubRecommendationService {
    List<Club> getRecommendationsForUser(Long userId);

}
