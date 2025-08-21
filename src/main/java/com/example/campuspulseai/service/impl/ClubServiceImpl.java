package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.common.util.IAuthUtils;
import com.example.campuspulseai.domain.dto.request.CreateClubRequest;
import com.example.campuspulseai.domain.dto.response.CreateClubResponse;
import com.example.campuspulseai.domain.dto.response.GetClubResponse;
import com.example.campuspulseai.domain.dto.response.GetEventResponse;
import com.example.campuspulseai.service.IClubService;
import com.example.campuspulseai.southbound.entity.Club;
import com.example.campuspulseai.southbound.entity.Group;
import com.example.campuspulseai.southbound.entity.User;
import com.example.campuspulseai.southbound.mapper.ClubMapper;
import com.example.campuspulseai.southbound.mapper.EventMapper;
import com.example.campuspulseai.southbound.repository.IClubRepository;
import com.example.campuspulseai.southbound.repository.IEventRepository;
import com.example.campuspulseai.southbound.repository.IUserRepository;
import com.example.campuspulseai.southbound.specification.IClubSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ClubServiceImpl implements IClubService {

    private final IAuthUtils authUtils;
    private final IClubRepository clubRepository;
    private final IEventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ClubMapper clubMapper;
    private final IClubSpecifications clubSpecifications;
    private final IUserRepository userRepository;

    @Override
    public CreateClubResponse createClub(CreateClubRequest createClubRequest) throws AccessDeniedException {
        User user = authUtils.getAuthenticatedUser();
        validateFirstClubCreation(user);
        Club club = clubMapper.toClub(createClubRequest, user);
        Club savedClub = clubRepository.save(club);
        user.setGroup(new Group(2L, "GROUP_ORGANIZERS", null));
        userRepository.save(user);
        return clubMapper.toCreateClubResponse(savedClub);
    }

    @Override
    public GetClubResponse getClubById(Long id) {
        Club club = findClubByIdOrThrow(id);
        List<GetEventResponse> events = eventMapper.toGetEventResponseList(eventRepository.findByClubId(club.getId()));
        return clubMapper.toGetClubResponse(club, events);
    }

    private Club findClubByIdOrThrow(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not found with id: " + id));
    }

    @Override
    public CreateClubResponse updateClub(Long id, CreateClubRequest updateClubRequest) throws AccessDeniedException {
        User currentUser = authUtils.getAuthenticatedUser();
        Club club = findClubByIdOrThrow(id);
        validateClubOwnership(club, currentUser);
        clubMapper.updateClubFromRequest(updateClubRequest, club);
        Club updatedClub = clubRepository.save(club);
        return clubMapper.toCreateClubResponse(updatedClub);
    }


    @Override
    public void deleteClubById(Long id) throws AccessDeniedException {
        User currentUser = authUtils.getAuthenticatedUser();
        Club club = findClubByIdOrThrow(id);
        validateClubOwnership(club, currentUser);
        clubRepository.delete(club);
    }

    private void validateClubOwnership(Club club, User currentUser) {
        if (!club.getOwner().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Only the organizer can perform this action on the club");
        }
    }

    @Override
    public Page<GetClubResponse> getClubs(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Club> spec = clubSpecifications.isActive();

        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and(clubSpecifications.nameContainsWordStartingWith(query));
        }

        Page<Club> clubs = clubRepository.findAll(spec, pageable);

        return clubs.map(club -> clubMapper.toGetClubResponse(
                club,
                eventMapper.toGetEventResponseList(eventRepository.findByClubId(club.getId()))
        ));
    }

    private void validateFirstClubCreation(User user) throws ResponseStatusException {
        Optional<Club> oldClub = clubRepository.findByOwnerId(user.getId());
        if (oldClub.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You already own a club");
        }
    }

}