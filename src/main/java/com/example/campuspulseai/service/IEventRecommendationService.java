package com.example.campuspulseai.service;

import java.util.List;

public interface IEventRecommendationService {
    List<Long> getRecommendedEventIds(Long userId);
}
