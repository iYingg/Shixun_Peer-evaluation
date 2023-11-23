package com.zjt.server.mapper;

import com.zjt.server.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Tao
* @description 针对表【course】的数据库操作Mapper
* @createDate 2023-11-16 10:47:23
* @Entity com.zjt.server.entity.Course
*/
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    String getTnameByCno(String cno);

    void insertCourse(Course course);

    void deleteByCno(String cno);
}




