package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByClubCategoryIn(List<String> category);
}
