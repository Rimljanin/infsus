package com.example.infsus.repository;

import com.example.infsus.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, String>, JpaSpecificationExecutor<Event> {

    List<Event> findByEventOwner_IdOrPlayersViaApp_Id(String ownerId, String playerId);

}
