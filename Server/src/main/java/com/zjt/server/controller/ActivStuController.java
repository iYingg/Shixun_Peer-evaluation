package com.zjt.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.HomeworkcommitService;
import com.zjt.server.util.StringUtil;
import com.zjt.server.service.PeerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activ/stu")
public class ActivStuController {

    @Autowired
    private PeerUserService peerUserService;

    @Autowired
    private HomeworkcommitService homeworkcommitService;


    /**
     * 学生信息列表
     * @param pageBean
     * @return
     */
    @PostMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String choosecno = pageBean.getCno().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        String HID = pageBean.getHID().trim();
        int chooseHID = Integer.parseInt(HID);
        Page<PeerUser> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {
            pageResult = peerUserService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<PeerUser>
                    ().inSql("no","SELECT no FROM peer_user JOIN sc ON peer_user.`NO`=sc.sno WHERE cno ='" + choosecno + "'"));
        }else {
            pageResult = peerUserService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<PeerUser>
                    ().inSql("no","SELECT no FROM peer_user JOIN sc ON peer_user.`NO`=sc.sno WHERE cno ='" + choosecno + "' and name like '%"+query+"%'"));
        }

        List<PeerUser> userList = pageResult.getRecords();
        List<ActivStuVO> activStuVOList = new ArrayList<>();
        for(PeerUser peerUser:userList){
            QueryWrapper<Homeworkcommit> wrapper = new QueryWrapper<>();
            wrapper.eq("HID",chooseHID)
                    .eq("Sno",peerUser.getNo());
            Homeworkcommit homeworkcommit = homeworkcommitService.getOne(wrapper);
            ActivStuVO activStuVO = new ActivStuVO(peerUser,homeworkcommit.getFinalscore());
            activStuVOList.add(activStuVO);

        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("userList",activStuVOList);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }

    /**
     * 修改学生作业分数
     * @param homeworkcommit
     * @return
     */
    @PostMapping("/edit")
    public R edit(@ModelAttribute Homeworkcommit homeworkcommit){
        homeworkcommitService.updateFinalscore(homeworkcommit.getHid(),homeworkcommit.getSno(),homeworkcommit.getFinalscore());
        return R.ok();
    }
}
