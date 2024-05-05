package com.example.infsus.model;

import com.example.infsus.model.superclass.IdSuperClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sport extends IdSuperClass {
    private String name;
}
