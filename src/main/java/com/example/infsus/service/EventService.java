package com.example.infsus.service;

import com.example.infsus.model.Event;
import com.example.infsus.model.User;
import com.example.infsus.repository.EventRepository;
import com.example.infsus.requests.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserService userService;

    public Event getEventById(String id) {
        return eventRepository.findById(id).get();
    }

    @Transactional
    public Event createEvent(EventRequest eventRequest) {
        Event event = new Event(eventRequest);
        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(EventRequest eventRequest,String id) {
        User user = userService.findUserByEmail();
        Event event = getEventById(id);
        if (event.getEventOwner().getId().equals(user.getId())) {
            event.setName(eventRequest.getName());
            event.setLocation(eventRequest.getLocation());
            event.setLocked(eventRequest.isLocked());
            event.setSport(eventRequest.getSport());
            event.setCurrentPeople(eventRequest.getCurrentPeople());
            event.setMaxPeople(eventRequest.getMaxPeople());
            event.setStartTime(eventRequest.getStartTime());
            event.setSport(eventRequest.getSport());

            return eventRepository.save(event);
        }
        throw new RuntimeException("User doesn't have permission to update this event");
    }
}
