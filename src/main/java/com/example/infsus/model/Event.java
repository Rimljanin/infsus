package com.example.infsus.model;

import com.example.infsus.model.superclass.IdSuperClass;
import com.example.infsus.requests.EventRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event")
public class Event extends IdSuperClass {

    private String name;

    @ManyToOne
    @JoinColumn(name = "eventowner_id")
    private User eventOwner;

    private int maxPeople;

    private int currentPeople;

    @ManyToOne
    private Location location;

    private LocalDateTime startTime;

    private boolean locked;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @ManyToMany
    @JoinTable(
            name = "event_players_via_app",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<User> playersViaApp;


    public Event(EventRequest eventRequest){
        this.name = eventRequest.getName();
        this.eventOwner = eventRequest.getEventOwner();
        this.maxPeople = eventRequest.getMaxPeople();
        this.currentPeople = eventRequest.getCurrentPeople();
        this.location = eventRequest.getLocation();
        this.startTime = eventRequest.getStartTime();
        this.locked = eventRequest.isLocked();
        this.sport = eventRequest.getSport();
    }
}
