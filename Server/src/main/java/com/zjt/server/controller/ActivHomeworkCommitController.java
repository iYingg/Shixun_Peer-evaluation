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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activ/commithomework")
public class ActivHomeworkCommitController {


    @Autowired
    HomeworkpublishService homeworkpublishService;

    @Autowired
    HomeworkcommitService homeworkcommitService;

    @Autowired
    HomeworkreviseService homeworkreviseService;

    /**
     * 显示已经提交的作业
     * @param pageBean
     * @return
     */
    @PostMapping("/list")
    public R listPublish(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        String choosecno = pageBean.getCno().trim();

        Page<Homeworkpublish> pageResult;
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

        List<Homeworkpublish> homeworkpublishList = pageResult.getRecords();
        List<HomeworkVO> homeworkpublishList2 = new ArrayList<>();
        int no = 1;
        for (Homeworkpublish homeworkpublish:homeworkpublishList){

            homeworkpublish.setDeadline2(DateForm.DatetoString(homeworkpublish.getDeadline()));
            homeworkpublish.setNo(no);
            no++;
            homeworkpublish.setIsCommit("Y");
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

    


    /**
     * 显示未提交的作业
     * @param pageBean
     * @return
     */
    @PostMapping("/unpublishlist")
    public R listUnpublish(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        String choosecno = pageBean.getCno().trim();

        Page<Homeworkpublish> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = homeworkpublishService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkpublish>
                    ().inSql("HID","select HID from homeworkpublish where cno = '"+choosecno+"' and HID not in (select hc.HID from homeworkcommit hc inner join homeworkpublish hp on hc.HID = hp.HID where cno = '"+choosecno+"' and sno = '"+currentUserNo+"')"));
        }else {
            pageResult = homeworkpublishService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkpublish>
                    ().inSql("HID","select HID from homeworkpublish where cno = '"+choosecno+"' and HID not in (select hc.HID from homeworkcommit hc inner join homeworkpublish hp on hc.HID = hp.HID where cno = '"+choosecno+"' and sno = '"+currentUserNo+"') and htitle like '%"+query+"%'"));
        }

        List<Homeworkpublish> homeworkpublishList = pageResult.getRecords();
        List<Homeworkpublish> homeworkpublishList2 = new ArrayList<>();
        int no = 1;
        for (Homeworkpublish homeworkpublish:homeworkpublishList){
            homeworkpublish.setDeadline2(DateForm.DatetoString(homeworkpublish.getDeadline()));
            homeworkpublish.setNo(no);
            no++;
            homeworkpublish.setIsCommit("N");
            homeworkpublishList2.add(homeworkpublish);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("homeworkpublishList",homeworkpublishList2);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }

    /**
     * 全部作业列表
     * @param pageBean
     * @return
     */
    @PostMapping("/listAll")
    public R listAll(@RequestBody PageBean pageBean) {
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

        List<Homeworkpublish> homeworkpublishList2 = new ArrayList<>();
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
            homeworkpublishList2.add(homeworkpublish);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("homeworkpublishList",homeworkpublishList2);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }


    /**
     * 下载作业附件
     * @param HID
     * @param httpServletResponse
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/downhomework{HID}/{Sno}")
    public String downhomework(@PathVariable String HID, @PathVariable String Sno, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException {
        int hid = Integer.parseInt(HID);
        if(homeworkcommitService.downhomework(hid,Sno,httpServletResponse)) {
            return RestBean.success("下载全部作业成功").asJsonString();
        }
        else return RestBean.failure(999, "添加失败").asJsonString();
    }


    /**
     * 提交作业
     * @param homeworkcommit
     * @return
     * @throws IOException
     */
    @PostMapping("/commit")
    public R commit(@ModelAttribute Homeworkcommit homeworkcommit) throws IOException {
        homeworkcommitService.insert(homeworkcommit.getHid(),homeworkcommit.getSno(),homeworkcommit.getAnswer(),homeworkcommit.getMultipartFile());
        return R.ok();
    }

    /**
     * 删除上交作业记录
     * @param homeworkcommit
     * @return
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Homeworkcommit homeworkcommit){
        homeworkcommitService.deleteByhidandsno(homeworkcommit.getHid(),homeworkcommit.getSno());
        return R.ok();
    }
}
