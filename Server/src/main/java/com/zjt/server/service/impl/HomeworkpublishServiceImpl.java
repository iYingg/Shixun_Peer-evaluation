package com.zjt.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjt.server.entity.Homeworkpublish;
import com.zjt.server.service.HomeworkpublishService;
import com.zjt.server.mapper.HomeworkpublishMapper;
import com.zjt.server.util.DateForm;
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
* @description 针对表【homeworkpublish】的数据库操作Service实现
* @createDate 2023-11-16 22:42:52
*/
@Service
public class HomeworkpublishServiceImpl extends ServiceImpl<HomeworkpublishMapper, Homeworkpublish>
    implements HomeworkpublishService{


    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private HomeworkpublishMapper homeworkpublishMapper;

    @Override
    public void upload(Homeworkpublish homework) throws IOException {
        Homeworkpublish homeworkpublish = new Homeworkpublish();
        homeworkpublish.setHtitle(homework.getHtitle());
        homeworkpublish.setHcontent(homework.getHcontent());
        homeworkpublish.setPublisher(homework.getPublisher());
        homeworkpublish.setCno(homework.getCno());

        Date date = DateForm.StringtoDate(homework.getDeadline2());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        homeworkpublish.setDeadline(calendar.getTime());
        if(homework.getHid()!=0){
            homeworkpublish.setHid(homework.getHid());
        }
        if (homework.getMultipartFile()!=null)
        {
            homeworkpublish.setFilename(homework.getMultipartFile().getOriginalFilename());
            homeworkpublish.setAnnex(homework.getMultipartFile().getBytes());
        }else{
            homeworkpublish.setFilename("");
            homeworkpublish.setAnnex(new byte[0]);
        }
        homeworkpublish.setHomeworkanswer(homework.getMultipartFile2().getBytes());
        homeworkpublish.setAnswerfilename(homework.getMultipartFile2().getOriginalFilename());
        this.saveOrUpdate(homeworkpublish);
    }

    @Override
    public boolean downhomework(int hid, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException {
        Homeworkpublish homeworkpublish = this.getById(hid);
        return fileUtils.downloadFile(homeworkpublish.getAnnex(),homeworkpublish.getFilename(),httpServletResponse);
    }

    @Override
    public void deleteByHID(int hid) {
        homeworkpublishMapper.deleteById(hid);
    }

    @Override
    public void edit(int hid, String htitle, String hcontent, int time, MultipartFile multipartFile) throws IOException {
        Homeworkpublish homeworkpublish = this.getById(hid);
        homeworkpublish.setHtitle(htitle);
        homeworkpublish.setHcontent(hcontent);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,time);
        homeworkpublish.setDeadline(calendar.getTime());
        homeworkpublish.setFilename(multipartFile.getOriginalFilename());
        homeworkpublish.setAnnex(multipartFile.getBytes());
        this.save(homeworkpublish);
    }
}




