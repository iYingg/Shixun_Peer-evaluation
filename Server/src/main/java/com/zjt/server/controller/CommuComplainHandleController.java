package com.zjt.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.ComplaincommitService;
import com.zjt.server.service.ComplainhandleService;
import com.zjt.server.service.PeerUserService;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/commu/handle")
public class CommuComplainHandleController {


    @Autowired
    ComplaincommitService complaincommitService;

    @Autowired
    PeerUserService peerUserService;

    @Autowired
    ComplainhandleService complainhandleService;

    @PostMapping("/listunrespond")
    public R listunrespond(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String cno = pageBean.getCno().trim();

        Page<Complaincommit> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = complaincommitService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Complaincommit>
                    ().inSql("ComplainID","select ComplainID from complaincommit where cno = '"+cno+"' and Respond = 'N'"));
        }else {
            pageResult = complaincommitService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Complaincommit>
                    ().inSql("ComplainID","select ComplainID from complaincommit where cno = '"+cno+"' and Respond = 'N' and Content like '%"+query+"%'"));
        }

        List<Complaincommit> complaincommitList = pageResult.getRecords();
        List<ComplainCommitVO> complaincommitList1 = new ArrayList<>();
        ComplainCommitVO complainCommitVO;
        Teaching teaching;
        PeerUser peerUser;
        for(Complaincommit complaincommit : complaincommitList){
            QueryWrapper<PeerUser> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("no",complaincommit.getSno());
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

        Page<Complainhandle> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = complainhandleService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Complainhandle>
                    ().inSql("HandleID","select HandleID from complainhandle natural join complaincommit where cno = '"+cno+"' and Respond = 'Y'"));
        }else {
            pageResult = complainhandleService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Complainhandle>
                    ().inSql("HandleID","select HandleID from complainhandle natural join complaincommit where cno = '"+cno+"' and Respond = 'Y' and Content like '%"+query+"%'"));
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
            QueryWrapper<PeerUser> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("no",complaincommit.getSno());
            peerUser = peerUserService.getOne(wrapper1);
            complainCommitVO = new ComplainCommitVO(complaincommit,complainhandle,peerUser.getName());
            complaincommitList1.add(complainCommitVO);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("complainlist",complaincommitList1);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }

    @PostMapping("/respond")
    public R respond(@RequestBody Complainhandle complainhandle){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        complainhandle.setHandledate(calendar.getTime());
        complainhandleService.save(complainhandle);
        complaincommitService.updaterespond(complainhandle.getComplainid());
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Complainhandle complainhandle){
        QueryWrapper<Complainhandle> wrapper = new QueryWrapper<>();
        wrapper.eq("ComplainID",complainhandle.getComplainid());
        complainhandleService.removeById(complainhandleService.getOne(wrapper).getHandleid());
        complaincommitService.updaterespond2(complainhandle.getComplainid());
        return R.ok();
    }
}
