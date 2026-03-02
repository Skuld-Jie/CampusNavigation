package com.example.campus.controller;

import com.example.campus.entity.NavNode;
import com.example.campus.service.NavNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private NavNodeService navNodeService;

    @GetMapping("/coordinates")
    public ResponseEntity<NavNode> getLocationByCoordinates(
            @RequestParam double lng,
            @RequestParam double lat) {

        NavNode location = navNodeService.getLocationByCoordinates(lng, lat);

        if (location != null) {
            return ResponseEntity.ok(location);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

