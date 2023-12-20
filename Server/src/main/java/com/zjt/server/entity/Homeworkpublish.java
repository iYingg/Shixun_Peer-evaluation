package com.zjt.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @TableName homeworkpublish
 */
@TableName(value ="homeworkpublish")
@Data
public class Homeworkpublish implements Serializable {
    /**
     * 作业ID
     */
    @TableId(value = "HID", type = IdType.AUTO)
    private Integer hid;

    /**
     * 作业标题
     */
    @TableField(value = "Htitle")
    private String htitle;

    /**
     * 作业说明
     */
    @TableField(value = "Hcontent")
    private String hcontent;

    /**
     * 发布人（tno）
     */
    @TableField(value = "Publisher")
    private String publisher;

    /**
     * 课程（cno）
     */
    @TableField(value = "Cno")
    private String cno;

    /**
     * 截止时间
     */
    @TableField(value = "Deadline")
    private Date deadline;

    /**
     * 附件名字
     */
    @TableField(value = "Filename")
    private String filename;

    /**
     * 附件
     */
    @TableField(value = "Annex")
    private byte[] annex;


    @TableField(value = "HomeworkAnswer")
    private byte[] homeworkanswer;

    @TableField(value = "AnswerFilename")
    private String answerfilename;



    /**
     * 前端传入的文件
     */
    @TableField(exist = false)
    private MultipartFile multipartFile;

    @TableField(exist = false)
    private MultipartFile multipartFile2;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 前端传来的deadline（字符串）
     */
    @TableField(exist = false)
    private String deadline2;

    /**
     * 计数
     */
    @TableField(exist = false)
    private int no;

    /**
     * 学生是否提交
     */
    @TableField(exist = false)
    private String isCommit;

    @TableField(exist = false)
    private String isLate;

}