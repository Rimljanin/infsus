package com.example.infsus.controller;

import com.example.infsus.model.Location;
import com.example.infsus.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("public")
    public List<Location> getLocations() {
        return locationService.getAllLocations();
    }
}
