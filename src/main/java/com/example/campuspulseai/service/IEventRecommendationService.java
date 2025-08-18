package com.example.campuspulseai.service;

import com.example.campuspulseai.southbound.entity.SuggestedOrganizerEvent;

import java.util.List;

public interface IEventRecommendationService {
    Long[] getRecommendedEventIds(Long userId);

    List<SuggestedOrganizerEvent> getSuggestedOrganizerEvents();
}
