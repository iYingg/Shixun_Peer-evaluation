package com.zjt.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.CourseService;
import com.zjt.server.service.PeerUserService;
import com.zjt.server.service.ScService;
import com.zjt.server.service.TeachingService;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activ/courseAdm")
public class ActivCourseAdmController {

    @Autowired
    CourseService courseService;

    @Autowired
    ScService scService;

    @Autowired
    TeachingService teachingService;

    @Autowired
    PeerUserService peerUserService;


    /**
     * 查找所有课程
     * @param pageBean
     * @return
     */
    @PostMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();


        Page<Course> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no "));
        }else {
            pageResult = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cname like '%"+query+"%'"));
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
     * 添加课程信息
     * 在course表添加课程信息
     * 在teaching表添加信息
     * @param course
     * @return
     */
    @PostMapping("/addCourse")
    public R addCourse(@RequestBody Course course){
        courseService.insertByCourse(course);
        Teaching teaching = new Teaching();
        teaching.setCno(course.getCno());
        teaching.setTno(course.getTno());
        teachingService.insertByTeaching(teaching);
        return R.ok();
    }


    /**
     *删除课程信息
     * @param cno
     * @return
     */
    @PostMapping("/delete")
    public R delete(@RequestBody String cno){
        cno = cno.substring(0,cno.length()-1);
        courseService.deleteByCourse(cno);
        teachingService.deleteByTeaching(cno);

        return R.ok();
    }


    /**
     * 检查cno是否重复
     * @param course
     * @return
     */
    @PostMapping("/checkNo")
    public R checkUserName(@RequestBody Course course){
        if(courseService.getById(course.getCno())==null){
            return R.ok();
        }else{
            return R.error();
        }
    }
}
