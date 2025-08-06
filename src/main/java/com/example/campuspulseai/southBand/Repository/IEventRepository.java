package com.example.campuspulseai.southBand.Repository;

import com.example.campuspulseai.southBand.Entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventRepository extends CrudRepository<Event, Long> {

}
