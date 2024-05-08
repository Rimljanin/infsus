package com.example.infsus.requests;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class UserRequest {

    private String name;
    private String lastName;
    private String userName;
}
