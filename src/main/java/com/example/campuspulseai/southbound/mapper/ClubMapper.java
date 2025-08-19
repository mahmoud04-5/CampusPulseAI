package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.domain.dto.response.GetClubResponse;
import com.example.campuspulseai.southbound.entity.Club;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClubMapper {
    @Mapping(source = "clubName", target = "name")
    @Mapping(source = "id", target = "clubId")
    @Mapping(source = "description", target = "description")
    GetClubResponse toDto(Club club);
    List<GetClubResponse> toDtoList(List<Club> clubs);
}
