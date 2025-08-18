package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.domain.dto.request.CreateClubRequest;
import com.example.campuspulseai.domain.dto.response.CreateClubResponse;
import com.example.campuspulseai.domain.dto.response.GetClubResponse;
import com.example.campuspulseai.domain.dto.response.GetEventResponse;
import com.example.campuspulseai.domain.dto.response.OrganizerResponse;
import com.example.campuspulseai.southbound.entity.Club;
import com.example.campuspulseai.southbound.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface ClubMapper {

    ClubMapper INSTANCE = Mappers.getMapper(ClubMapper.class);

    @Mapping(source = "club.id", target = "clubId")
    CreateClubResponse toCreateClubResponse(Club club);

    @Mapping(source = "club.id", target = "clubId")
    @Mapping(source = "club.owner.id", target = "organizerResponse.id")
    @Mapping(source = "club.owner.firstName", target = "organizerResponse.firstName")
    @Mapping(source = "club.owner.lastName", target = "organizerResponse.lastName")
    GetClubResponse toGetClubResponse(Club club, List<GetEventResponse> events);


    void updateClubFromRequest(CreateClubRequest request, @MappingTarget Club club);

    @Mapping(source = "request.clubName", target = "name")   // 👈 Fix
    @Mapping(source = "request.clubDescription", target = "description") // 👈 Fix
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "user")
    Club toClub(CreateClubRequest request, User user);
}
