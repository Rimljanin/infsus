package com.example.infsus.service;

import com.example.infsus.model.User;
import com.example.infsus.repository.UserRepository;
import com.example.infsus.requests.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(String id){
        return userRepository.findById(id).get();
    }

    @Transactional
    public User createUser(UserRequest userRequest){
        User user = new User(userRequest);
        String email = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwtToken = (Jwt) authentication.getPrincipal();
            email = jwtToken.getClaim("email");
            user.setEmail(email);

        } else {
            throw new RuntimeException("Nedozvoljen pristup");
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UserRequest userRequest) {
        String email = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwtToken = (Jwt) authentication.getPrincipal();
            email = jwtToken.getClaim("email");
            User user = userRepository.findByEmail(email);
            if (user != null) {
                user.setUserName(userRequest.getUserName());
                user.setName(userRequest.getName());
                user.setLastName(userRequest.getLastName());
                return user;
            } else {
                throw new RuntimeException("Klijent nije pronađen");
            }
        } else {
            throw new RuntimeException("Nedozvoljen pristup");
        }
    }

    @Transactional
    public User findUserByEmail() {
        String email = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwtToken = (Jwt) authentication.getPrincipal();
            email = jwtToken.getClaim("email");
            User user = userRepository.findByEmail(email);
            if (user != null) {
                return user;
            } else {
                throw new RuntimeException("Klijent nije pronađen");
            }
        } else {
            throw new RuntimeException("Nedozvoljen pristup");
        }
    }

}
