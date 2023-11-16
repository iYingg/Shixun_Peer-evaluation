package com.zjt.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activ/courseStu")
public class ActivCourseStuController {

    //@Autowired
    //CourseService courseService;


//    /**
//     * 查找未选的课程
//     * @param pageBean
//     * @return
//     */
//    @PostMapping("/list")
//    public R list(@RequestBody PageBean pageBean) {
//        String query = pageBean.getQuery().trim();
//        String currentUserNo = pageBean.getUserNo().trim();
//        Page<Course> pageResult;
//        if(!StringUtil.isNotEmpty(query))
//        {
//            pageResult = courseService.page(new Page<>
//                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
//                    ().like("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno NOT in (SELECT cno FROM course NATURAL JOIN sc WHERE sno="+currentUserNo+") "));
//        }else {
//            pageResult = courseService.page(new Page<>
//                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Course>
//                    ().inSql("cno","select cno from course natural join teaching inner join peer_user on tno=no WHERE cno NOT in (SELECT cno FROM course NATURAL JOIN sc WHERE sno="+currentUserNo+") and cname like '%"+query+"%'"));
//        }
//
//        List<Course> courseList = pageResult.getRecords();
//        Map<String,Object> resultMap=new HashMap<>();
//        resultMap.put("courseList",courseList);
//        resultMap.put("total",pageResult.getTotal());
//        return R.ok(resultMap);
//    }

}
