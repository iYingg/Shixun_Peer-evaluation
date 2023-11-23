package com.zjt.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjt.server.entity.Course;
import com.zjt.server.service.CourseService;
import com.zjt.server.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Tao
* @description 针对表【course】的数据库操作Service实现
* @createDate 2023-11-16 10:47:23
*/
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
    implements CourseService{

    @Autowired
    CourseMapper courseMapper;

    @Override
    public String getTnameByCno(String cno) {
        return courseMapper.getTnameByCno(cno);
    }

    @Override
    public void insertByCourse(Course course) {
        courseMapper.insertCourse(course);
    }

    @Override
    public void deleteByCourse(String  cno) {
        courseMapper.deleteByCno(cno);
    }

}




