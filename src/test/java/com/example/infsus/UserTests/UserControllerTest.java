package com.example.infsus.UserTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

import com.example.infsus.controller.UserController;
import com.example.infsus.model.User;
import com.example.infsus.requests.UserRequest;
import com.example.infsus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

@Slf4j
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserControllerTest.SecurityConfig.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;
    private UserRequest userRequest;

    @TestConfiguration
    @EnableWebSecurity
    static class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests().anyRequest().permitAll();
            return http.build();
        }
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setName("Trpimir");
        user.setLastName("Trpo");
        user.setUserName("Trp");
        user.setEmail("trp@example.com");

        userRequest = new UserRequest();
        userRequest.setName("Trpimir");
        userRequest.setLastName("Trpo");
        userRequest.setUserName("Trp");
    }

    @Test
    @WithMockUser
    public void testGetProfile() throws Exception {
        when(userService.getUserById("1")).thenReturn(user);

        MvcResult result = mockMvc.perform(get("/user/public/getProfile/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        User returnedUser = new ObjectMapper().readValue(content, User.class);

        assertEquals(user.getName(), returnedUser.getName());
        assertEquals(user.getLastName(), returnedUser.getLastName());
        assertEquals(user.getUserName(), returnedUser.getUserName());
        assertEquals(user.getEmail(), returnedUser.getEmail());
    }


}
