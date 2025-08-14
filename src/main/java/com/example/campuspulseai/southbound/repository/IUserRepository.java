package com.example.campuspulseai.southbound.repository;

import com.example.campuspulseai.southbound.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IUserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
