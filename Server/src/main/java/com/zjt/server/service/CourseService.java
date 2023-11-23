package com.zjt.server.service;

import com.zjt.server.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Tao
* @description 针对表【course】的数据库操作Service
* @createDate 2023-11-16 10:47:23
*/
public interface CourseService extends IService<Course> {

    String getTnameByCno(String cno);

    void insertByCourse(Course course);

    void deleteByCourse(String  cno);


}
