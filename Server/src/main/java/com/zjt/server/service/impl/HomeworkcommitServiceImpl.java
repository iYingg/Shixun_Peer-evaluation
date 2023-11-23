package com.zjt.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjt.server.entity.Homeworkcommit;
import com.zjt.server.entity.Homeworkrevise;
import com.zjt.server.service.HomeworkcommitService;
import com.zjt.server.mapper.HomeworkcommitMapper;
import com.zjt.server.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

/**
* @author Tao
* @description 针对表【homeworkcommit】的数据库操作Service实现
* @createDate 2023-11-17 15:35:55
*/
@Service
public class HomeworkcommitServiceImpl extends ServiceImpl<HomeworkcommitMapper, Homeworkcommit>
    implements HomeworkcommitService{


    @Autowired
    private FileUtils fileUtils;

    @Autowired
    HomeworkcommitMapper homeworkcommitMapper;

    @Override
    public void insert(int hid, String sno, String answer, MultipartFile multipartFile) throws IOException {
        Homeworkcommit homeworkcommit = new Homeworkcommit();
        homeworkcommit.setHid(hid);
        homeworkcommit.setSno(sno);
        homeworkcommit.setAnswer(answer);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        homeworkcommit.setCommitdate(calendar.getTime());
        if (multipartFile!=null) {
            homeworkcommit.setAnnex(multipartFile.getBytes());
            homeworkcommit.setFilename(multipartFile.getOriginalFilename());
        }else {
            homeworkcommit.setFilename("");
            homeworkcommit.setAnnex(new byte[0]);
        }
        homeworkcommitMapper.insertByhomeworkCommit(homeworkcommit);
    }

    @Override
    public void deleteByhidandsno(int hid, String sno) {
        homeworkcommitMapper.deleteByhidandSno(hid,sno);
    }

    @Override
    public boolean downhomework(int hid,String Sno, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException {
        QueryWrapper<Homeworkcommit> wrapper = new QueryWrapper<>();
        wrapper.eq("HID",hid)
                .eq("Sno",Sno);
        Homeworkcommit homeworkcommit = this.getOne(wrapper);
        return fileUtils.downloadFile(homeworkcommit.getAnnex(),homeworkcommit.getFilename(),httpServletResponse);
    }

    @Override
    public Homeworkcommit getAnswer(Homeworkrevise homeworkrevise) {
        QueryWrapper<Homeworkcommit> wrapper = new QueryWrapper<>();
        wrapper.eq("HID",homeworkrevise.getHid())
                .eq("Sno",homeworkrevise.getCommitsno());
        return this.getOne(wrapper);
    }
}




