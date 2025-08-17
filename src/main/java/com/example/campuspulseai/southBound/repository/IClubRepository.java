package com.example.campuspulseai.southBound.repository;

import com.example.campuspulseai.southBound.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClubRepository extends JpaRepository<Club,Long> {
    List<Club> findByClubCategoryIn(List<String> category);
    Optional<Club> findByClubNameIgnoreCase(String clubName);

}
