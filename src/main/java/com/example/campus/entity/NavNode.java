package com.example.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
@Data
@TableName("nav_node")
public class NavNode {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Double lng;
    private Double lat;
    private String category;
    private String description;
    private String image;
    private String imageUrl;
    private String openTimeSchool;
    private String openTimeHoliday;
}


