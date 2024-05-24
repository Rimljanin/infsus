package com.example.infsus.repository;

import com.example.infsus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    User findByUserName(String username);

    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
}
