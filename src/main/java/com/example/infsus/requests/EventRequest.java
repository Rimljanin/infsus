package com.example.infsus.requests;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventRequest {
    private String name;
    private int maxPeople;
    private int currentPeople;
    private String locationId;
    private LocalDateTime startTime;
    private boolean locked;
    private String sportId;
}
