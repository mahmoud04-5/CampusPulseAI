package com.example.campuspulseai.southBound.repository;

import com.example.campuspulseai.southBound.entity.Club;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface IClubRepository extends CrudRepository<Club, Long> {
    Optional<Club> findByOwnerId(Long ownerId);
    List<Club> findByClubCategoryIn(List<String> category);
}
