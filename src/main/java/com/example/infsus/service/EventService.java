package com.example.infsus.service;

import com.example.infsus.helpers.EventSpecification;
import com.example.infsus.model.*;
import com.example.infsus.repository.EventPlayerRepository;
import com.example.infsus.repository.EventRepository;
import com.example.infsus.repository.LocationRepository;
import com.example.infsus.repository.SportRepository;
import com.example.infsus.requests.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EventPlayerRepository eventPlayerRepository;

    public Event getEventById(String id) {
        return eventRepository.findById(id).get();
    }

    @Transactional
    public Event createEvent(EventRequest eventRequest) {
        User user = userService.findUserByUsername();
        Optional<Location> location = locationRepository.findById(eventRequest.getLocationId());
        Optional<Sport> sport = sportRepository.findById(eventRequest.getSportId());

        Event event = new Event(
                eventRequest.getName(),
                user,
                eventRequest.getMaxPeople(),
                eventRequest.getCurrentPeople(),
                location.get(),
                eventRequest.getStartTime(),
                eventRequest.isLocked(),
                sport.get()
        );

        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(EventRequest eventRequest,String id) {
        User user = userService.findUserByUsername();
        Optional<Location> location = locationRepository.findById(eventRequest.getLocationId());
        Optional<Sport> sport = sportRepository.findById(eventRequest.getSportId());
        Event event = getEventById(id);
        if (event.getEventOwner().getId().equals(user.getId())) {
            event.setName(eventRequest.getName());
            event.setLocation(location.get());
            event.setLocked(eventRequest.isLocked());
            event.setSport(sport.get());
            event.setCurrentPeople(eventRequest.getCurrentPeople());
            event.setMaxPeople(eventRequest.getMaxPeople());
            event.setStartTime(eventRequest.getStartTime());

            return eventRepository.save(event);
        }
        throw new RuntimeException("User doesn't have permission to update this event");
    }

    @Transactional
    public ResponseEntity<String> joinEvent(String id) {
        User user = userService.findUserByUsername();
        Event event = getEventById(id);

        if(event.isLocked()){
            throw new RuntimeException("Event is locked");
        }
        if (event.getMaxPeople() > event.getCurrentPeople()){
            if(event.getPlayersViaApp().contains(user)){
                return new ResponseEntity<>("You can't join this event, you are already in this game", HttpStatus.OK);
            }
            EventPlayer eventPlayer= new EventPlayer(event, user);
            eventPlayerRepository.save(eventPlayer);
            event.setCurrentPeople(event.getCurrentPeople()+1);
        }
        else{
            return new ResponseEntity<>("Event is full", HttpStatus.OK);
        }
        return new ResponseEntity<>("Joined Event", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> lockEvent(String id) {
        User user = userService.findUserByUsername();
        Event event = getEventById(id);
        if (event.getEventOwner().getId().equals(user.getId())) {
            event.setLocked(true);
        }else{
            throw new RuntimeException("User doesn't have permission to lock this event");
        }
        return new ResponseEntity<>("Event locked", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<String> unlockEvent(String id) {
        User user = userService.findUserByUsername();
        Event event = getEventById(id);
        if (event.getEventOwner().getId().equals(user.getId())) {
            event.setLocked(false);
        }else{
        throw new RuntimeException("User doesn't have permission to lock this event");
        }
        return new ResponseEntity<>("Event unlocked", HttpStatus.OK);
    }

    @Transactional
    public void deleteEvent(String id) {
        User user = userService.findUserByUsername();
        Event event = getEventById(id);
        if (event.getEventOwner().getId().equals(user.getId())) {
            eventRepository.delete(event);
        }else{
            throw new RuntimeException("User doesn't have permission to delete this event");
        }
    }

    public Page<Event> filterEvents(String name, String locationId, String sportId, LocalDateTime startTime, Boolean myGames, Pageable pageable) {

        User currentUser= userService.findUserByUsername();

        Location location=null;
        Sport sport=null;
        if(locationId!=null){
            location=locationRepository.findById(locationId).get();
        }
        if(sportId!=null){
            sport=sportRepository.findById(sportId).get();
        }


        Specification<Event> spec = EventSpecification.getEventsByCriteria(name, location, sport, startTime, myGames, currentUser);
        return eventRepository.findAll(spec, pageable);
    }

    public List<Event> findEventsByOwnerOrPlayer(String userId) {
        return eventRepository.findByEventOwner_IdOrPlayersViaApp_Id(userId, userId);
    }

    public Event findEventById(String id) {
        return eventRepository.findById(id).orElse(null);
    }


}
