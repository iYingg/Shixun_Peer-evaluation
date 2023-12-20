package com.zjt.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.HomeworkcommitService;
import com.zjt.server.service.HomeworkpublishService;
import com.zjt.server.service.HomeworkreviseService;
import com.zjt.server.util.DateForm;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activ/score")
public class ActivScoreController {


    @Autowired
    HomeworkpublishService homeworkpublishService;

    @Autowired
    HomeworkcommitService homeworkcommitService;

    @Autowired
    HomeworkreviseService homeworkreviseService;

    /**
     * 全部作业列表
     * @param pageBean
     * @return
     */
    @PostMapping("/listHomework")
    public R list(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        String choosecno = pageBean.getCno().trim();

        Page<Homeworkpublish> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = homeworkpublishService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkpublish>
                    ().inSql("HID","select HID from homeworkpublish where cno = '"+choosecno+"'"));
        }else {
            pageResult = homeworkpublishService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkpublish>
                    ().inSql("HID","select HID from homeworkpublish where cno = '"+choosecno+"' and htitle like '%"+query+"%'"));
        }

        List<Homeworkpublish> homeworkpublishList = pageResult.getRecords();

        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = homeworkpublishService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkpublish>
                    ().inSql("HID","select hc.HID from homeworkcommit hc inner join homeworkpublish hp on hc.HID = hp.HID where cno = '"+choosecno+"' and sno = '"+currentUserNo+"'"));
        }else {
            pageResult = homeworkpublishService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkpublish>
                    ().inSql("HID","select hc.HID from homeworkcommit hc inner join homeworkpublish hp on hc.HID = hp.HID where cno = '"+choosecno+"' and sno = '"+currentUserNo+"' and htitle like '%"+query+"%'"));
        }

        List<Homeworkpublish> chomeworkpublishList = pageResult.getRecords();

        List<HomeworkVO> homeworkpublishList2 = new ArrayList<>();
        int no = 1;
        for (Homeworkpublish homeworkpublish:homeworkpublishList){

            homeworkpublish.setDeadline2(DateForm.DatetoString(homeworkpublish.getDeadline()));
            homeworkpublish.setNo(no);
            no++;
            homeworkpublish.setIsCommit("N");
            for (Homeworkpublish homeworkpublish1:chomeworkpublishList){
                if(homeworkpublish1.getHid()==homeworkpublish.getHid()){
                    homeworkpublish.setIsCommit("Y");
                    break;
                }
            }
            HomeworkVO homeworkVO = new HomeworkVO(homeworkpublish);

            //答案
            QueryWrapper<Homeworkcommit> wrapper = new QueryWrapper<>();
            wrapper.eq("HID",homeworkpublish.getHid())
                    .eq("Sno",currentUserNo);

            List<Homeworkcommit> homeworkcommitList = homeworkcommitService.list(wrapper);
            for (Homeworkcommit homeworkcommit : homeworkcommitList){
                homeworkVO.setAnswer(homeworkcommit.getAnswer());
                homeworkVO.setFilename2(homeworkcommit.getFilename());
                homeworkVO.setCommitdate(DateForm.DatetoString(homeworkcommit.getCommitdate()));
            }

            //互评情况
            QueryWrapper<Homeworkrevise> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("HID",homeworkpublish.getHid())
                    .eq("Reviser",currentUserNo)
                    .eq("`Status`","Y");
            homeworkVO.setHasrevise(homeworkreviseService.count(wrapper2));

            QueryWrapper<Homeworkrevise> wrapper3 = new QueryWrapper<>();
            wrapper3.eq("HID",homeworkpublish.getHid())
                    .eq("Reviser",currentUserNo)
                    .eq("`Status`","N");
            homeworkVO.setUnrevise(homeworkreviseService.count(wrapper3));

            QueryWrapper<Homeworkrevise> wrapper4 = new QueryWrapper<>();
            wrapper4.eq("HID",homeworkpublish.getHid())
                    .eq("Reviser",currentUserNo);
            homeworkVO.setAllrevise(homeworkreviseService.count(wrapper4));
            homeworkVO.setRevisestatus(" " + homeworkVO.getHasrevise()+" / "+homeworkVO.getAllrevise());
            homeworkpublishList2.add(homeworkVO);

        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("homeworkpublishList",homeworkpublishList2);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }


}
