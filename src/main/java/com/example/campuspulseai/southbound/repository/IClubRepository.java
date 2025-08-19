package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.Club;
import com.example.campuspulseai.southbound.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClubRepository extends JpaRepository<Club, Long> , JpaSpecificationExecutor {
    Optional<Club> findByOwnerId(Long ownerId);
    Page<Club> findByNameContainingIgnoreCaseAndIsActiveTrue(String query, Pageable pageable);
    Page<Club> findByIsActiveTrue(Pageable pageable);

    Optional<Club> findByClubNameIgnoreCase(String clubName);

}
