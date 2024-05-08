package com.example.infsus.model;

import com.example.infsus.model.superclass.IdSuperClass;
import com.example.infsus.requests.UserRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user")
@Entity
public class User extends IdSuperClass {

    @Column(name = "name")
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "eventOwner")
    private List<Event> ownedEvents;

    @JsonIgnore
    @ManyToMany(mappedBy = "playersViaApp")
    private List<Event> eventsViaApp;


    public User(UserRequest userRequest){
        this.name = userRequest.getName();
        this.lastName = userRequest.getLastName();
        this.userName = userRequest.getUserName();
    }

}
