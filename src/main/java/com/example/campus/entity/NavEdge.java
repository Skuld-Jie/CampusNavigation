package com.example.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("nav_edge")
public class NavEdge {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long fromNode;
    private Long toNode;
    private Double distance;
    private Double walkSpeed;
    private Double bikeSpeed;
}
