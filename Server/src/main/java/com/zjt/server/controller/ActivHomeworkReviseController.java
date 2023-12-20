package com.zjt.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.*;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/activ/revisehomework")
public class ActivHomeworkReviseController {

    @Autowired
    private HomeworkreviseService homeworkreviseService;

    @Autowired
    private  HomeworkcommitService homeworkcommitService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeachingService teachingService;

    @Autowired
    private HomeworkpublishService homeworkpublishService;

    /**
     * 列出已经批改的作业
     * @param pageBean
     * @return
     */
    @PostMapping("/listrevise")
    public R listRevise(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        String HID = pageBean.getHID().trim();
        int chooseHID = Integer.parseInt(HID);

        Page<Homeworkrevise> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("id","SELECT id FROM homeworkrevise WHERE `Status` = 'Y'  and HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"'"));
        }else {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("id","SELECT id FROM homeworkrevise natural join homeworkpublish WHERE `Status` = 'Y'  and HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"' and htitle like '%"+query+"%'"));
        }

        List<Homeworkrevise> homeworkreviseList = pageResult.getRecords();
        List<Homeworkrevise> homeworkreviseList2 = new ArrayList<>();
        int no = 1;
        for(Homeworkrevise homeworkrevise : homeworkreviseList){
            homeworkrevise.setNo(no);
            no++;
            homeworkreviseList2.add(homeworkrevise);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("homeworkreviseList",homeworkreviseList2);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }


    /**
     * 列出没有批改的作业
     * @param pageBean
     * @return
     */
    @PostMapping("/listdisrevise")
    public R listDisrevise(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        String HID = pageBean.getHID().trim();
        int chooseHID = Integer.parseInt(HID);

        Page<Homeworkrevise> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("id","SELECT id FROM homeworkrevise WHERE `Status` = 'N'  and HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"'"));
        }else {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("id","SELECT id FROM homeworkrevise natural join homeworkpublish WHERE `Status` = 'N'  and HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"' and htitle like '%"+query+"%'"));
        }

        List<Homeworkrevise> homeworkreviseList = pageResult.getRecords();
        List<Homeworkrevise> homeworkreviseList2 = new ArrayList<>();
        int no = 1;
        for(Homeworkrevise homeworkrevise : homeworkreviseList){
            homeworkrevise.setNo(no);
            no++;
            homeworkreviseList2.add(homeworkrevise);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("homeworkreviseList",homeworkreviseList2);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }

    /**
     * 列出全部作业
     * @param pageBean
     * @return
     */
    @PostMapping("/listall")
    public R listall(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        String HID = pageBean.getHID().trim();
        int chooseHID = Integer.parseInt(HID);

        Page<Homeworkrevise> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("id","SELECT id FROM homeworkrevise WHERE HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"'"));
        }else {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("id","SELECT id FROM homeworkrevise natural join homeworkpublish WHERE HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"' and htitle like '%"+query+"%'"));
        }

        List<Homeworkrevise> homeworkreviseList = pageResult.getRecords();
        List<Homeworkrevise> homeworkreviseList2 = new ArrayList<>();
        int no = 1;
        for(Homeworkrevise homeworkrevise : homeworkreviseList){
            homeworkrevise.setNo(no);
            no++;
            homeworkreviseList2.add(homeworkrevise);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("homeworkreviseList",homeworkreviseList2);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }



    /**
     * 提交批改作业结果
     * @param homeworkrevise
     * @return
     */
    @PostMapping("/revise")
    public R revise(@ModelAttribute Homeworkrevise homeworkrevise){
        homeworkreviseService.updateRevise(homeworkrevise.getHid(),homeworkrevise.getReviser(),homeworkrevise.getCommitsno(),homeworkrevise.getScore(),homeworkrevise.getRemark());
        return R.ok();
    }


    /**
     * 撤销批改作业
     * @param homeworkrevise
     * @return
     */
    @PostMapping("/revoke")
    public R revoke(@RequestBody Homeworkrevise homeworkrevise){
        homeworkreviseService.revoke(homeworkrevise.getHid(),homeworkrevise.getReviser(),homeworkrevise.getCommitsno());
        return R.ok();
    }


    /**
     * 获取答案
     * @param homeworkrevise
     * @return
     */
    @PostMapping("/getAnswer")
    public R getAnswer(@RequestBody Homeworkrevise homeworkrevise){
        //更新最终成绩
        QueryWrapper<Homeworkrevise> wrapper = new QueryWrapper<>();
        wrapper.eq("HID",homeworkrevise.getHid())
                .eq("CommitSno",homeworkrevise.getCommitsno());
        List<Homeworkrevise> homeworkreviseList = homeworkreviseService.list(wrapper);
        int sum = 0;
        int count = 0;
        for (Homeworkrevise homeworkrevise1:homeworkreviseList){
            if (homeworkrevise1.getScore()!=null) {
                sum += homeworkrevise1.getScore();
                count++;
            }
        }
        QueryWrapper<Homeworkrevise> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("HID",homeworkrevise.getHid())
                .eq("Reviser",homeworkrevise.getCommitsno())
                .eq("Status","Y");
        int count1 = homeworkreviseService.count(wrapper2);
        int finalscore = (int) Math.round((sum/count)*(1-(3-count1)*0.03));
        UpdateWrapper<Homeworkcommit> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("HID",homeworkrevise.getHid())
                .eq("Sno",homeworkrevise.getCommitsno())
                .set("FinalScore",finalscore);
        homeworkcommitService.update(null,updateWrapper);
        Homeworkcommit homeworkcommit =  homeworkcommitService.getAnswer(homeworkrevise);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("homeworkcommit",homeworkcommit);
        return R.ok(resultMap);
    }



    /**
     * 查看评语
     * @param pageBean
     * @return
     */
    @PostMapping("/listremark")
    public R listRemark(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        String HID = pageBean.getHID().trim();
        int chooseHID = Integer.parseInt(HID);

        Page<Homeworkrevise> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("id","SELECT id FROM homeworkrevise WHERE `Status` = 'Y'  and HID = "+chooseHID+" AND CommitSno = '"+currentUserNo+"'"));
        }else {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("id","SELECT id FROM homeworkrevise natural join homeworkpublish WHERE `Status` = 'Y'  and HID = "+chooseHID+" AND CommitSno = '"+currentUserNo+"' and htitle like '%"+query+"%'"));
        }

        List<Homeworkrevise> homeworkreviseList = pageResult.getRecords();
        List<Homeworkrevise> homeworkreviseList2 = new ArrayList<>();
        int no = 1;
        for(Homeworkrevise homeworkrevise : homeworkreviseList){
            homeworkrevise.setNo(no);
            no++;
            homeworkreviseList2.add(homeworkrevise);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("homeworkreviseList",homeworkreviseList2);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }




    @PostMapping("/listChosen")
    public R listChosen(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();

        //分配作业
        QueryWrapper<Homeworkcommit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sno",currentUserNo);
        List<Homeworkcommit> homeworkcommitList = homeworkcommitService.list(queryWrapper);
        List<Homeworkcommit> homeworkcommitList2 = new ArrayList<>();
        Homeworkrevise homeworkrevise = new Homeworkrevise();
        UpdateWrapper<Homeworkcommit> updateWrapper = new UpdateWrapper<>();
        for (Homeworkcommit homeworkcommit:homeworkcommitList){
            if(homeworkcommit.getIsallocate().equals("N")){
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("hid",homeworkcommit.getHid());
                homeworkcommitList2 = homeworkcommitService.list(queryWrapper);
                if(homeworkcommitList2.size()>3){
                    //随机分配
                    Collections.shuffle(homeworkcommitList2);
                    for (Homeworkcommit homeworkcommit1:homeworkcommitList2){
                        if(!homeworkcommit1.getSno().equals(currentUserNo)){
                            homeworkrevise.setReviser(currentUserNo);
                            homeworkrevise.setCommitsno(homeworkcommit1.getSno());
                            homeworkrevise.setHid(homeworkcommit1.getHid());
                            homeworkrevise.setStatus("N");
                            homeworkreviseService.insert(homeworkrevise);
                        }
                    }
                }
                updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("hid",homeworkcommit.getHid())
                        .eq("sno",currentUserNo).set("isAllocate","Y");
                homeworkcommitService.update(null,updateWrapper);
            }
        }

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
                    ().inSql("id","select id from homeworkrevise natural join homeworkpublish where cno = '"+choosecno+"' and reviser = '"+currentUserNo+"' and `status`='Y'" ));
            Page<Homeworkrevise> pageResult5;
            pageResult5 = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("id","select id from homeworkrevise natural join homeworkpublish where cno = '"+choosecno+"' and reviser = '"+currentUserNo+"'" ));

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
}
