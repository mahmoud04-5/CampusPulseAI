package com.example.campuspulseai.southBound.repository;

import com.example.campuspulseai.southBound.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByOwnerId(Long ownerId);
}
