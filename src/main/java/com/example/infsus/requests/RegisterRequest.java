package com.example.infsus.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String lastName;
    private String userName;
    private String email;
    private String password;
}
