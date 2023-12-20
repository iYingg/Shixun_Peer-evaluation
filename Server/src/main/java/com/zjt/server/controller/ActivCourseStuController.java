package com.zjt.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.*;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
@RequestMapping("/activ/courseStu")
public class ActivCourseStuController {

    @Autowired
    CourseService courseService;

    @Autowired
    ScService scService;

    @Autowired
    TeachingService teachingService;

    @Autowired
    HomeworkpublishService homeworkpublishService;

    @Autowired
    HomeworkreviseService homeworkreviseService;


    /**
     * 查找未选的课程
     * @param pageBean
     * @return
     */
    @PostMapping("/listUnchosen")
    public R listUnchosen(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        Page<Course> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno NOT in (SELECT cno FROM course NATURAL JOIN sc WHERE sno="+currentUserNo+") "));
        }else {
            pageResult = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno NOT in (SELECT cno FROM course NATURAL JOIN sc WHERE sno="+currentUserNo+") and cname like '%"+query+"%'"));
        }

        List<Course> courseList = pageResult.getRecords();
        List<CourseVO> courseVOList = new ArrayList<>();
        for (Course course:courseList){
            String tname = courseService.getTnameByCno(course.getCno());
            String tno = teachingService.getTnoByCno(course.getCno()) ;
            CourseVO courseVO = new CourseVO(course,tname,tno);
            courseVO.setIsChosen(0);
            courseVOList.add(courseVO);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("courseList",courseVOList);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }


    /**
     * 查询已经选择的列表
     * @param pageBean
     * @return
     */
    @PostMapping("/listChosen")
    public R listChosen(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();

        Page<Course> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno in (SELECT cno FROM course NATURAL JOIN sc WHERE sno="+currentUserNo+") "));
        }else {
            pageResult = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno in (SELECT cno FROM course NATURAL JOIN sc WHERE sno="+currentUserNo+") and cname like '%"+query+"%'"));
        }

        List<Course> courseList = pageResult.getRecords();
        List<CourseVO> courseVOList = new ArrayList<>();
        for (Course course:courseList){
            String tname = courseService.getTnameByCno(course.getCno());
            String tno = teachingService.getTnoByCno(course.getCno()) ;
            CourseVO courseVO = new CourseVO(course,tname,tno);
            courseVO.setIsChosen(1);
            //查看完成作业情况
            String choosecno = courseVO.getCno();
            Page<Homeworkpublish> pageResult2;
            pageResult2 = homeworkpublishService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkpublish>
                    ().inSql("HID","select hc.HID from homeworkcommit hc inner join homeworkpublish hp on hc.HID = hp.HID where cno = '"+choosecno+"' and sno = '"+currentUserNo+"'"));
            Page<Homeworkpublish> pageResult3;
            pageResult3 = homeworkpublishService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkpublish>
                    ().inSql("HID","select HID from homeworkpublish where cno = '"+choosecno+"'"));
            courseVO.setHomeworkstatus(" "+pageResult2.getTotal()+" / "+pageResult3.getTotal());
            courseVO.setHascommit(Integer.parseInt(String.valueOf(pageResult2.getTotal())));
            courseVO.setAllcommit(Integer.parseInt(String.valueOf(pageResult3.getTotal())));



            //查看有多少作业需要互评
            Page<Homeworkrevise> pageResult4;
            pageResult4 = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("commitsno","select commitsno from homeworkrevise natural join homeworkpublish where cno = '"+choosecno+"' and reviser = '"+currentUserNo+"' and `status`='Y'" ));
            Page<Homeworkrevise> pageResult5;
            pageResult5 = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("commitsno","select commitsno from homeworkrevise natural join homeworkpublish where cno = '"+choosecno+"' and reviser = '"+currentUserNo+"'" ));

            courseVO.setHasrevise(Integer.parseInt(String.valueOf(pageResult4.getTotal())));
            courseVO.setAllrevise(Integer.parseInt(String.valueOf(pageResult5.getTotal())));
            courseVO.setRevisestatus(" "+pageResult4.getTotal()+" / "+pageResult5.getTotal());
            courseVOList.add(courseVO);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("courseList",courseVOList);
        resultMap.put("total",pageResult.getTotal());


        return R.ok(resultMap);
    }


    /**
     * 查找所有课程
     * @param pageBean
     * @return
     */
    @PostMapping("/listAll")
    public R list(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();

        Page<Course> pageResult;
        Page<Course> pageResult2;
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

        if(!StringUtil.isNotEmpty(query))
        {

            pageResult2 = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno in (SELECT cno FROM course NATURAL JOIN sc WHERE sno="+currentUserNo+") "));
        }else {
            pageResult2 = courseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno in (SELECT cno FROM course NATURAL JOIN sc WHERE sno="+currentUserNo+") and cname like '%"+query+"%'"));
        }

        List<Course> courseList = pageResult.getRecords();
        List<Course> courseList2 = pageResult2.getRecords();
        List<CourseVO> courseVOList = new ArrayList<>();
        for (Course course:courseList){
            String tname = courseService.getTnameByCno(course.getCno());
            String tno = teachingService.getTnoByCno(course.getCno()) ;
            CourseVO courseVO = new CourseVO(course,tname,tno);
            for (Course course2:courseList2){
                if(course2.getCno().equals(course.getCno())) {
                    courseVO.setIsChosen(1);
                    break;
                }
            }
            courseVOList.add(courseVO);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("courseList",courseVOList);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }


    /**
     * 学生加入课程
     * 在sc表插入选课信息
     * @param course
     * @return
     */
    @Transactional
    @PostMapping("/joinCourse")
    public R join(@RequestBody Course course){
        scService.insertByCnoandSno(course.getCno().trim(),course.getUserNo().trim());
        return R.ok();
    }

    /**
     * 学生推出课程
     * 在sc表中删除信息
     * @param course
     * @return
     */
    @Transactional
    @PostMapping("/dropCourse")
    public R drop(@RequestBody Course course){
        scService.dropByCnoandSno(course.getCno(),course.getUserNo());
        return R.ok();
    }
}
