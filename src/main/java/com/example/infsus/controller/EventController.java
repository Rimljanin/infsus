package com.example.infsus.controller;

import com.example.infsus.model.Event;
import com.example.infsus.model.Location;
import com.example.infsus.model.Sport;
import com.example.infsus.model.User;
import com.example.infsus.requests.EventRequest;
import com.example.infsus.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("event")
public class EventController {

    @Autowired
    private EventService eventService;


    @GetMapping("/events")
    public Page<Event> getEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Location location,
            @RequestParam(required = false) Sport sport,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) Boolean myGames,
            @PageableDefault(size = 20) Pageable pageable) {

        return eventService.filterEvents(name, location, sport, startTime, myGames, pageable);
    }


    @GetMapping("/public/events/user/{userId}")
    public ResponseEntity<List<Event>> getEventsByUser(@PathVariable String userId) {
        List<Event> events = eventService.findEventsByOwnerOrPlayer(userId);
        return ResponseEntity.ok(events);
    }

    @PostMapping("createEvent")
    public Event createEvent(@RequestBody EventRequest eventRequest) {
        return eventService.createEvent(eventRequest);
    }

    @PutMapping("updateEvent/{id}")
    public Event updateEvent(@RequestBody EventRequest eventRequest, @PathVariable String id) {
        return eventService.updateEvent(eventRequest,id);
    }

    @PostMapping("joinEvent/{id}")
    public ResponseEntity<?> joinEvent(@PathVariable String id) {
        return new ResponseEntity<>(eventService.joinEvent(id), HttpStatus.OK);
    }

    @PostMapping("lockEvent/{id}")
    public ResponseEntity<?> lockEvent(@PathVariable String id) {
        return new ResponseEntity<>(eventService.lockEvent(id), HttpStatus.OK);
    }

    @PostMapping("unlockEvent/{id}")
    public ResponseEntity<?> unlockEvent(@PathVariable String id) {
        return new ResponseEntity<>(eventService.unlockEvent(id), HttpStatus.OK);
    }

    @DeleteMapping("deleteEvent/{id}")
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

}
