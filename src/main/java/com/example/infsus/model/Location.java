package com.example.infsus.model;

import com.example.infsus.model.superclass.IdSuperClass;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="location")
@Entity
public class Location extends IdSuperClass {
    private String name;
    private String addres;
    private String city;


    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private List<Event> events;

}
