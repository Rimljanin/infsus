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
@Table(name="sport")
@Entity
public class Sport extends IdSuperClass {
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "sport")
    private List<Event> events;

}
