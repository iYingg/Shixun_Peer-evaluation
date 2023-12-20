package com.zjt.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjt.server.entity.*;
import com.zjt.server.service.HomeworkpublishService;
import com.zjt.server.util.DateForm;
import com.zjt.server.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activ/publishhomework")
public class ActivHomeworkPublishController {

    @Autowired
    private HomeworkpublishService homeworkpublishService;


    /**
     * 显示所有自己的作业
     * @param pageBean
     * @return
     */
    @PostMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        String query = pageBean.getQuery().trim();
        String currentUserNo = pageBean.getUserNo().trim();
        String choosecno = pageBean.getCno().trim();

        Page<Homeworkpublish> pageResult;
        if(!StringUtil.isNotEmpty(query))
        {

            pageResult = homeworkpublishService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkpublish>
                    ().inSql("HID","select HID from homeworkpublish where cno = '"+choosecno+"' and publisher = '"+currentUserNo+"'"));
        }else {
            pageResult = homeworkpublishService.page(new Page<>
                    (pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<Homeworkpublish>
                    ().inSql("HID","select HID from homeworkpublish where cno = '"+choosecno+"' and publisher = '"+currentUserNo+"' and htitle like '%"+query+"%'"));
        }

        List<Homeworkpublish> homeworkpublishList = pageResult.getRecords();
        List<Homeworkpublish> homeworkpublishList2 = new ArrayList<>();
        int no = 1;
        for (Homeworkpublish homeworkpublish:homeworkpublishList){
            homeworkpublish.setDeadline2(DateForm.DatetoString(homeworkpublish.getDeadline()));
            homeworkpublish.setNo(no);
            no++;
            homeworkpublishList2.add(homeworkpublish);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("homeworkpublishList",homeworkpublishList2);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }



    /**
     * 发布作业
     * @param homeworkpublish
     * @return
     * @throws IOException
     */
    @Transactional
    @PostMapping("/publish")
    public R publish(@ModelAttribute Homeworkpublish homeworkpublish) throws IOException {;
        homeworkpublishService.upload(homeworkpublish);
        return R.ok();
    }


    /**
     * 删除作业记录
     * @param HID
     * @return
     */
    @Transactional
    @PostMapping("/delete")
    public R delete(@RequestBody String HID){
        HID = HID.substring(0,HID.length()-1);
        int hid = Integer.parseInt(HID);
        homeworkpublishService.deleteByHID(hid);
        return R.ok();
    }



    /**
     * 老师修改作业
     * @param time
     * @param htitle
     * @param hcontent
     * @param multipartFile
     * @param hid
     * @return
     * @throws IOException
     */
    @Transactional
    @PostMapping("/edit")
    public R edit(@RequestParam int time,@RequestParam String htitle,@RequestParam String hcontent, @RequestParam MultipartFile multipartFile,@RequestParam int hid) throws IOException {
        homeworkpublishService.edit(hid,htitle,hcontent,time,multipartFile);
        return R.ok();
    }


    /**
     * 下载附件
     * @param HID
     * @param httpServletResponse
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/downhomework{HID}")
    public String downhomework(@PathVariable String HID, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException {
        int hid = Integer.parseInt(HID);
        if(homeworkpublishService.downhomework(hid,httpServletResponse)) {
            return RestBean.success("下载全部作业成功").asJsonString();
        }
        else return RestBean.failure(999, "添加失败").asJsonString();
    }


    /**
     * 根据hid查找数据
     * @param hid
     * @return
     */
    @GetMapping("/{hid}")
    public R findById(@PathVariable(value = "hid")int hid){
        Homeworkpublish homeworkpublish = homeworkpublishService.getById(hid);
        Map<String,Object> map=new HashMap<>();
        map.put("homeworkpublish",homeworkpublish);
        return R.ok(map);
    }

}
