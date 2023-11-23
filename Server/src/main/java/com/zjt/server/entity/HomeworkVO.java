package com.zjt.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class HomeworkVO {

    private Integer hid;

    private String sno;

    private String answer;

    private String commitdate;

    private String filename;

    private byte[] annex;

    private String deadline2;

    private String filename2;

    private Date deadline;

    private int no;

    private String isCommit;

    private MultipartFile multipartFile;

    private MultipartFile multipartFile2;

    private String htitle;

    private String hcontent;

    private String publisher;

    private String cno;

    private Integer hasrevise;

    private Integer allrevise;

    private Integer unrevise;

    private String revisestatus;
    public HomeworkVO(Homeworkpublish homeworkpublish){
        this.no = homeworkpublish.getNo();
        this.hid = homeworkpublish.getHid();
        this.htitle = homeworkpublish.getHtitle();
        this.hcontent = homeworkpublish.getHcontent();
        this.publisher = homeworkpublish.getPublisher();
        this.deadline2 = homeworkpublish.getDeadline2();
        this.multipartFile = homeworkpublish.getMultipartFile();
        this.filename = homeworkpublish.getFilename();
        this.annex = homeworkpublish.getAnnex();
        this.isCommit = homeworkpublish.getIsCommit();
        this.cno = homeworkpublish.getCno();

    }
}
