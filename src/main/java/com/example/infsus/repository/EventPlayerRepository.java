package com.example.infsus.repository;


import com.example.infsus.model.EventPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPlayerRepository extends JpaRepository<EventPlayer, String> {
}
