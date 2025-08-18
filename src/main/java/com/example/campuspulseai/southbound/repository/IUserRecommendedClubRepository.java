package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.SuggestedUserClubs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserRecommendedClubRepository extends JpaRepository<SuggestedUserClubs, Long> {
    List<SuggestedUserClubs> findByUserId(Long userId);
}
