package com.example.infsus.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {




    private String keycloakIssuerUri="https://lemur-12.cloud-iam.com/auth/realms/ibg";

    private JwtIssuerAuthenticationManagerResolver authenticationManagerResolver;

    @PostConstruct
    public void init() {
        authenticationManagerResolver = new JwtIssuerAuthenticationManagerResolver(keycloakIssuerUri);
    }

    String[] controllers = {"sport","event","location","user"};
    String[] noAuthEndpoints = {"public"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        List<String> requestMatchers = new ArrayList<>();
        for (String controller : controllers) {
            for (String noAuthEndpoint : noAuthEndpoints) {
                requestMatchers.add(controller + "/" + noAuthEndpoint);
                requestMatchers.add(controller + "/" + noAuthEndpoint + "/**");
            }
        }

        // Dodavanje Swagger UI i povezanih putanja u listu dozvoljenih bez autentifikacije
        String[] swaggerPaths = {"/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**"};
        requestMatchers.addAll(Arrays.asList(swaggerPaths));

        http.cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET,
                        requestMatchers.toArray(new String[0]))
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST,
                        requestMatchers.toArray(new String[0]))
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.PUT,
                        requestMatchers.toArray(new String[0]))
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.DELETE,
                        requestMatchers.toArray(new String[0]))
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.authenticationManagerResolver(authenticationManagerResolver));
        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
