package com.example.campuspulseai.southbound.specification;

import com.example.campuspulseai.southbound.entity.Club;
import org.springframework.data.jpa.domain.Specification;

public interface IClubSpecifications {
    public  Specification<Club> isActive();
    public  Specification<Club> nameContainsWordStartingWith(String keyword);
}
