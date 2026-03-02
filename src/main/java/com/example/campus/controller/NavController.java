package com.example.campus.controller;

import com.example.campus.entity.NavNode;
import com.example.campus.enums.TravelMode;
import com.example.campus.mapper.NavNodeMapper;
import com.example.campus.service.NavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nav")
@CrossOrigin
public class NavController {

    @Autowired
    private NavService navService;
    @Autowired
    private NavNodeMapper nodeMapper;


      // 查询所有导航节点（用于下拉框）

    @GetMapping("/nodes")
    public List<NavNode> listNodes() {
        return nodeMapper.selectList(null);
    }

    // 最短路径
    @GetMapping("/path")
    public Map<String, Object> path(
            @RequestParam Long start,
            @RequestParam Long end,
            @RequestParam TravelMode mode) {

        List<NavNode> path = navService.findPath(start, end);

        double distance = navService.calcDistance(path);
        double time = distance / mode.getSpeed(); // 秒

        Map<String, Object> res = new HashMap<>();
        res.put("path", path);
        res.put("distance", distance);
        res.put("time", time);

        return res;
    }
}