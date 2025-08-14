package com.example.campuspulseai.southBound.repository;

import com.example.campuspulseai.southBound.entity.UserEvent;
import com.example.campuspulseai.southBound.entity.UserEventId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserEventRepository extends JpaRepository<UserEvent, UserEventId> {
    List<UserEvent> findByUserId(Long id);
    List<UserEvent> findByEventId(UserEventId id);
}
