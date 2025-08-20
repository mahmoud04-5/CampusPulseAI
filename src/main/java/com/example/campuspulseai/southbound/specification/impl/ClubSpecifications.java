package com.example.campuspulseai.southbound.specification.impl;

import com.example.campuspulseai.southbound.entity.Club;
import com.example.campuspulseai.southbound.repository.IClubRepository;
import com.example.campuspulseai.southbound.specification.IClubSpecifications;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ClubSpecifications implements IClubSpecifications {

    public  Specification<Club> isActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isActive"));
    }

    public  Specification<Club> nameContainsWordStartingWith(String keyword) {
        return (root, query, cb) -> {
            String lowerKeyword = keyword.toLowerCase() + "%"; // match prefix
            return cb.or(
                    cb.like(cb.lower(root.get("name").as(String.class)), lowerKeyword), // starts with keyword
                    cb.like(cb.lower(root.get("name").as(String.class)), "% " + lowerKeyword) // word starts with keyword
            );
        };
    }

}
