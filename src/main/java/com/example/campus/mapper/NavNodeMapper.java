package com.example.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.campus.entity.NavNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface NavNodeMapper extends BaseMapper<NavNode> {

    @Select("SELECT description, openTimeSchool, openTimeHoliday FROM nav_node WHERE id = #{id}")
    NavNode selectLocationInfo(@Param("id") Long id);

    @Select("SELECT * FROM nav_node WHERE id = #{id}")
    NavNode selectByIdWithAllInfo(@Param("id") Long id);

    // 根据坐标查找最近的地点，添加id作为第二排序条件避免相同坐标时的不确定性
    @Select("SELECT * FROM nav_node ORDER BY SQRT(POWER(lng - #{lng}, 2) + POWER(lat - #{lat}, 2)), id LIMIT 1")
    NavNode selectByCoordinates(@Param("lng") double lng, @Param("lat") double lat);
}

