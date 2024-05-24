package com.example.infsus.service;

import com.example.infsus.model.User;
import com.example.infsus.repository.UserRepository;
import com.example.infsus.requests.UserRequest;
import com.example.infsus.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public User getUserById(String id){
        return userRepository.findById(id).get();
    }

    @Transactional
    public User save(User user){
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Transactional
    public User updateUser(UserRequest userRequest) {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getCredentials() instanceof String) {
                String token = (String) authentication.getCredentials();
                username = jwtTokenUtil.getUsernameFromToken(token);
                User user = userRepository.findByUserName(username);
                if (user != null) {
                    user.setUserName(userRequest.getUserName());
                    user.setName(userRequest.getName());
                    user.setLastName(userRequest.getLastName());
                    return user;
                } else {
                    throw new RuntimeException("Klijent nije pronađen");
                }
            } else {
                throw new RuntimeException("Nedozvoljen pristup: Credentials nisu String");
            }
        } else {
            throw new RuntimeException("Nedozvoljen pristup");
        }
    }

    @Transactional
    public User findUserByUsername() {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getCredentials() instanceof String) {
                String token = (String) authentication.getCredentials();
                username = jwtTokenUtil.getUsernameFromToken(token);
                User user = userRepository.findByUserName(username);
                if (user != null) {
                    return user;
                } else {
                    throw new RuntimeException("Klijent nije pronađen");
                }
            } else {
                throw new RuntimeException("Nedozvoljen pristup: Credentials nisu String");
            }
        } else {
            throw new RuntimeException("Nedozvoljen pristup: Authentication je null");
        }
    }

}
