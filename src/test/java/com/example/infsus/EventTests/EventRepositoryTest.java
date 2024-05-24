package com.example.infsus.EventTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.infsus.model.Event;
import com.example.infsus.model.User;
import com.example.infsus.repository.EventRepository;
import com.example.infsus.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EventRepositoryTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsService userDetailsService;

    private User eventOwner;
    private User player;
    private Event event1;
    private Event event2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        eventOwner = new User();
        eventOwner.setId("ownerId");
        eventOwner.setUserName("eventOwner");
        eventOwner.setPassword("password");

        player = new User();
        player.setId("playerId");
        player.setUserName("player");
        player.setPassword("password");

        event1 = new Event();
        event1.setId("eventId1");
        event1.setName("Event 1");
        event1.setEventOwner(eventOwner);
        event1.setMaxPeople(10);
        event1.setCurrentPeople(5);
        event1.setStartTime(LocalDateTime.now());
        event1.setLocked(false);

        event2 = new Event();
        event2.setId("eventId2");
        event2.setName("Event 2");
        event2.setEventOwner(eventOwner);
        event2.setMaxPeople(20);
        event2.setCurrentPeople(15);
        event2.setStartTime(LocalDateTime.now());
        event2.setLocked(true);
        event2.setPlayersViaApp(Arrays.asList(player));

        when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenAnswer(invocation -> {
            UserDetails userDetails = invocation.getArgument(0);
            return "mockedTokenForUser_" + userDetails.getUsername();
        });

        when(userDetailsService.loadUserByUsername(anyString())).thenAnswer(invocation -> {
            String username = invocation.getArgument(0);
            if ("eventOwner".equals(username)) {
                return new org.springframework.security.core.userdetails.User(eventOwner.getUserName(), eventOwner.getPassword(), Collections.emptyList());
            } else if ("player".equals(username)) {
                return new org.springframework.security.core.userdetails.User(player.getUserName(), player.getPassword(), Collections.emptyList());
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        });
    }

    @Test
    public void testFindByEventOwner_IdOrPlayersViaApp_Id() {
        when(eventRepository.findByEventOwner_IdOrPlayersViaApp_Id(anyString(), anyString()))
                .thenReturn(Arrays.asList(event1, event2));

        List<Event> events = eventRepository.findByEventOwner_IdOrPlayersViaApp_Id("ownerId", "playerId");

        assertEquals(2, events.size());
        assertEquals("Event 1", events.get(0).getName());
        assertEquals("Event 2", events.get(1).getName());
    }
}
