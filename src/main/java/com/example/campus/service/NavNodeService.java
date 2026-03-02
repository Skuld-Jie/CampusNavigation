package com.example.campus.service;

import com.example.campus.entity.NavNode;
import com.example.campus.mapper.NavNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NavNodeService {

    @Autowired
    private NavNodeMapper navNodeMapper;

    // 根据坐标查询地点
    public NavNode getLocationByCoordinates(double lng, double lat) {

        return navNodeMapper.selectByCoordinates(lng, lat);
    }

}


