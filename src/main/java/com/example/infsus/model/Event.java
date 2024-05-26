package com.example.infsus.model;

import com.example.infsus.model.superclass.IdSuperClass;
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

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "eventowner_id")
    private User eventOwner;

    @Column(name = "maxpeople")
    private int maxPeople;

    @Column(name = "currentpeople")
    private int currentPeople;

    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @Column(name = "starttime")
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

    public Event(String name, User eventOwner, int maxPeople, int currentPeople, Location location,
                 LocalDateTime startTime, boolean locked, Sport sport) {
        this.name = name;
        this.eventOwner = eventOwner;
        this.maxPeople = maxPeople;
        this.currentPeople = currentPeople;
        this.location = location;
        this.startTime = startTime;
        this.locked = locked;
        this.sport = sport;
    }
}
