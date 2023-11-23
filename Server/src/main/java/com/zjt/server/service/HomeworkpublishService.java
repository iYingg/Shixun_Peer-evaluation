package com.zjt.server.service;

import com.zjt.server.entity.Homeworkpublish;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
* @author Tao
* @description 针对表【homeworkpublish】的数据库操作Service
* @createDate 2023-11-16 22:42:52
*/
public interface HomeworkpublishService extends IService<Homeworkpublish> {


    void upload(Homeworkpublish homeworkpublish) throws IOException;

    boolean downhomework(int hid, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException;

    void deleteByHID(int hid);

    void edit(int hid, String htitle, String hcontent, int time, MultipartFile multipartFile) throws IOException;
}
