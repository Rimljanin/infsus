package com.example.infsus.requests;

import com.example.infsus.model.Location;
import com.example.infsus.model.Sport;
import com.example.infsus.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventRequest {
    private String name;
    private User eventOwner;
    private int maxPeople;
    private int currentPeople;
    private Location location;
    private LocalDateTime startTime;
    private boolean locked;
    private Sport sport;
}
