package com.example.infsus.SportTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

import com.example.infsus.controller.SportController;
import com.example.infsus.model.Sport;
import com.example.infsus.service.SportService;
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

import java.util.Arrays;
import java.util.List;

@WebMvcTest(SportController.class)
@ContextConfiguration(classes = {SportControllerTest.SecurityConfig.class})
public class SportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportService sportService;

    private List<Sport> sportList;

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

        Sport sport1 = new Sport();
        sport1.setName("Nogomet");
        Sport sport2 = new Sport();
        sport2.setName("Košarka");

        sportList = Arrays.asList(sport1, sport2);
    }

    @Test
    @WithMockUser
    public void testGetAllSports() throws Exception {
        when(sportService.getAllSports()).thenReturn(sportList);

        mockMvc.perform(get("/sport/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Nogomet"))
                .andExpect(jsonPath("$[1].name").value("Košarka"));
    }
}
