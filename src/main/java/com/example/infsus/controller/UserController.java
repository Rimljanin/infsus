package com.example.infsus.controller;

import com.example.infsus.model.User;
import com.example.infsus.requests.UserRequest;
import com.example.infsus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("public/getProfile/{id}")
    public User getProfile(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PutMapping("updateProfile")
    public User updateProfile(@RequestBody UserRequest userRequest) {
        return userService.updateUser(userRequest);
    }
}
