package com.example.campuspulseai.southbound.mapper;

import com.example.campuspulseai.southbound.entity.Club;
import com.example.campuspulseai.southbound.entity.SuggestedUserClubs;
import com.example.campuspulseai.southbound.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserClubMapper {

    @Mapping(target = "id", ignore = true)  // Ignore the generated ID
    @Mapping(target = "user", source = "user")
    @Mapping(target = "club", source = "club")
    SuggestedUserClubs toSuggestedUserClubs(User user, Club club);
}
