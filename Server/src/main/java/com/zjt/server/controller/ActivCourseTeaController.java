package com.zjt.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.CourseService;
import com.zjt.server.service.ScService;
import com.zjt.server.service.TeachingService;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activ/courseTea")
public class ActivCourseTeaController {

    @Autowired
    CourseService courseService;

    @Autowired
    ScService scService;

    @Autowired
    TeachingService teachingService;


    /**
     * 查找未教的课程
     * @param pageBean
     * @return
     */
    @PostMapping("/listUnteaching")
    public R listUnTeaching(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        Page<Course> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno NOT in (SELECT cno FROM course NATURAL JOIN teaching WHERE tno="+currentUserNo+") "));
        }else {
            pageResult = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno NOT in (SELECT cno FROM course NATURAL JOIN teaching WHERE tno="+currentUserNo+") and cname like '%"+query+"%'"));
        }

        List<Course> courseList = pageResult.getRecords();
        List<CourseVO> courseVOList = new ArrayList<>();
        for (Course course:courseList){
            String tname = courseService.getTnameByCno(course.getCno());
            String tno = teachingService.getTnoByCno(course.getCno()) ;
            CourseVO courseVO = new CourseVO(course,tname,tno);
            courseVOList.add(courseVO);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("courseList",courseVOList);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }


    /**
     * 查询教授的列表
     * @param pageBean
     * @return
     */
    @PostMapping("/listTeaching")
    public R listTeaching(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        Page<Course> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno in (SELECT cno FROM course NATURAL JOIN teaching WHERE tno="+currentUserNo+") "));
        }else {
            pageResult = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno in (SELECT cno FROM course NATURAL JOIN teaching WHERE tno="+currentUserNo+") and cname like '%"+query+"%'"));
        }

        List<Course> courseList = pageResult.getRecords();
        List<CourseVO> courseVOList = new ArrayList<>();


        for (Course course:courseList){
            QueryWrapper<Sc> wrapper = new QueryWrapper<>();
            String tname = courseService.getTnameByCno(course.getCno());
            String tno = teachingService.getTnoByCno(course.getCno()) ;
            wrapper.eq("cno",course.getCno());
            int count = scService.count(wrapper);
            CourseVO courseVO = new CourseVO(course,tname,tno);
            courseVO.setCount(count);
            courseVOList.add(courseVO);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("courseList",courseVOList);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }
}
