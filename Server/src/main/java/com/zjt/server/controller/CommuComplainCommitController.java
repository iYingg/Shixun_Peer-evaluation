package com.zjt.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.ComplaincommitService;
import com.zjt.server.service.ComplainhandleService;
import com.zjt.server.service.PeerUserService;
import com.zjt.server.service.TeachingService;
import com.zjt.server.util.DateForm;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/commu/complain")
public class CommuComplainCommitController {


    @Autowired
    ComplaincommitService complaincommitService;

    @Autowired
    TeachingService teachingService;

    @Autowired
    PeerUserService peerUserService;

    @Autowired
    ComplainhandleService complainhandleService;

    @PostMapping("/listcomplain")
    public R listcomplain(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String cno = pageBean.getCno().trim();
        String sno = pageBean.getUserNo().trim();

        Page<Complaincommit> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = complaincommitService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Complaincommit>
                    ().inSql("ComplainID","select ComplainID from complaincommit where cno = '"+cno+"' and Sno = '"+sno+"'"));
        }else {
            pageResult = complaincommitService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Complaincommit>
                    ().inSql("ComplainID","select ComplainID from complaincommit where cno = '"+cno+"' and Sno = '"+sno+"' and Content like '%"+query+"%'"));
        }

        List<Complaincommit> complaincommitList = pageResult.getRecords();
        List<ComplainCommitVO> complaincommitList1 = new ArrayList<>();
        ComplainCommitVO complainCommitVO;
        Teaching teaching;
        PeerUser peerUser;
        for(Complaincommit complaincommit : complaincommitList){
            QueryWrapper<Teaching> wrapper = new QueryWrapper<>();
            wrapper.eq("cno",complaincommit.getCno());
            teaching = teachingService.getOne(wrapper);
            QueryWrapper<PeerUser> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("no",teaching.getTno());
            peerUser = peerUserService.getOne(wrapper1);
            complainCommitVO = new ComplainCommitVO(complaincommit,peerUser.getName());
            complaincommitList1.add(complainCommitVO);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("complainlist",complaincommitList1);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }

    @PostMapping("/listrespond")
    public R listrespond(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String cno = pageBean.getCno().trim();
        String sno = pageBean.getUserNo().trim();

        Page<Complainhandle> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = complainhandleService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Complainhandle>
                    ().inSql("HandleID","select HandleID from complainhandle natural join complaincommit where cno = '"+cno+"' and Sno = '"+sno+"'"));
        }else {
            pageResult = complainhandleService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Complainhandle>
                    ().inSql("HandleID","select HandleID from complainhandle natural join complaincommit where cno = '"+cno+"' and Sno = '"+sno+"' and Content like '%"+query+"%'"));
        }

        List<Complainhandle> complainhandles = pageResult.getRecords();
        List<ComplainCommitVO> complaincommitList1 = new ArrayList<>();
        ComplainCommitVO complainCommitVO;
        Complaincommit complaincommit;
        Teaching teaching;
        PeerUser peerUser;
        for(Complainhandle complainhandle : complainhandles){
            QueryWrapper<Complaincommit> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("ComplainID",complainhandle.getComplainid());
            complaincommit = complaincommitService.getOne(wrapper2);
            QueryWrapper<Teaching> wrapper = new QueryWrapper<>();
            wrapper.eq("cno",complaincommit.getCno());
            teaching = teachingService.getOne(wrapper);
            QueryWrapper<PeerUser> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("no",teaching.getTno());
            peerUser = peerUserService.getOne(wrapper1);
            complainCommitVO = new ComplainCommitVO(complaincommit,complainhandle,peerUser.getName());
            complaincommitList1.add(complainCommitVO);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("complainlist",complaincommitList1);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }


    @PostMapping("/commit")
    public R commit(@RequestBody Complaincommit complaincommit){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        complaincommit.setDate(calendar.getTime());
        complaincommit.setRespond("N");
        complaincommitService.save(complaincommit);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Complaincommit complaincommit){
        complaincommitService.removeById(complaincommit.getComplainid());
        return R.ok();
    }
}
