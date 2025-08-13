package com.example.campuspulseai.southBound.repository;

import com.example.campuspulseai.southBound.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventRepository extends CrudRepository<Event, Long> {

}
