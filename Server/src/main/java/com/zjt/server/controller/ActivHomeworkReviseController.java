package com.zjt.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.HomeworkcommitService;
import com.zjt.server.service.HomeworkreviseService;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activ/revisehomework")
public class ActivHomeworkReviseController {

    @Autowired
    private HomeworkreviseService homeworkreviseService;

    @Autowired
    private  HomeworkcommitService homeworkcommitService;

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
                    ().inSql("CommitSno","SELECT CommitSno FROM homeworkrevise WHERE `Status` = 'Y'  and HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"'"));
        }else {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("CommitSno","SELECT CommitSno FROM homeworkrevise natural join homeworkpublish WHERE `Status` = 'Y'  and HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"' and htitle like '%"+query+"%'"));
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
                    ().inSql("CommitSno","SELECT CommitSno FROM homeworkrevise WHERE `Status` = 'N'  and HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"'"));
        }else {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("CommitSno","SELECT CommitSno FROM homeworkrevise natural join homeworkpublish WHERE `Status` = 'N'  and HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"' and htitle like '%"+query+"%'"));
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
                    ().inSql("CommitSno","SELECT CommitSno FROM homeworkrevise WHERE HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"'"));
        }else {
            pageResult = homeworkreviseService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkrevise>
                    ().inSql("CommitSno","SELECT CommitSno FROM homeworkrevise natural join homeworkpublish WHERE HID = "+chooseHID+" AND Reviser = '"+currentUserNo+"' and htitle like '%"+query+"%'"));
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
     * @param HID
     * @param reviser
     * @param commitsno
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
        Homeworkcommit homeworkcommit =  homeworkcommitService.getAnswer(homeworkrevise);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("homeworkcommit",homeworkcommit);
        return R.ok(resultMap);
    }

}
