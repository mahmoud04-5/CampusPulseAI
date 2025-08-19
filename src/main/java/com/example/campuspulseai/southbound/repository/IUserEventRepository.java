package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.UserEvent;
import com.example.campuspulseai.southbound.entity.UserEventId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserEventRepository extends JpaRepository<UserEvent, UserEventId> {
    List<UserEvent> findByUserId(Long id);

    List<UserEvent> findByEventId(Long id);
}
