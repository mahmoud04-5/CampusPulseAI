package com.example.campuspulseai.service;

public interface IEventRecommendationService {
    Long[] getRecommendedEventIds(Long userId, int limit);
}
