package com.example.infsus.controller;

import com.example.infsus.model.Sport;
import com.example.infsus.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("sport")
public class SportController {

    @Autowired
    private SportService sportService;

    @GetMapping("public")
    public List<Sport> getAllSports() {
        return sportService.getAllSports();
    }
}
