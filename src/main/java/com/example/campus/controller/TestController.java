package com.example.campus.controller;

import com.example.campus.entity.NavNode;
import com.example.campus.mapper.NavNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private NavNodeMapper nodeMapper;

    @GetMapping("/nodes")
    public ResponseEntity<?> getNodes() {
        try {
            List<NavNode> nodes = nodeMapper.selectList(null);
            return ResponseEntity.ok(nodes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving nodes: " + e.getMessage());
        }
    }
}