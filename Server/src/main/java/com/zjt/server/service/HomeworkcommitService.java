package com.zjt.server.service;

import com.zjt.server.entity.Homeworkcommit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjt.server.entity.Homeworkrevise;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
* @author Tao
* @description 针对表【homeworkcommit】的数据库操作Service
* @createDate 2023-11-17 15:35:55
*/
public interface HomeworkcommitService extends IService<Homeworkcommit> {

    void insert(int hid, String sno, String answer, MultipartFile multipartFile) throws IOException;

    void deleteByhidandsno(int hid, String sno);

    boolean downhomework(int hid,String Sno, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException;

    Homeworkcommit getAnswer(Homeworkrevise homeworkrevise);
}
