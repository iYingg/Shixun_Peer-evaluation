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
 * @TableName homeworkcommit
 */
@TableName(value ="homeworkcommit")
@Data
public class Homeworkcommit implements Serializable {
    /**
     * 作业ID
     */
    @TableId(value = "HID")
    private Integer hid;

    /**
     * 提交学生（Sno）
     */
    @TableId(value = "Sno")
    private String sno;

    /**
     * 提交答案
     */
    @TableField(value = "Answer")
    private String answer;

    /**
     * 提交时间
     */
    @TableField(value = "CommitDate")
    private Date commitdate;

    /**
     * 提交文件名
     */
    @TableField(value = "FileName")
    private String filename;

    /**
     * 提交附件
     */
    @TableField(value = "Annex")
    private byte[] annex;

    @TableField(value = "FinalScore")
    private Integer finalscore;

    @TableField(value = "isAllocate")
    private String isallocate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    /**
     * 前端传入的文件
     */
    @TableField(exist = false)
    private MultipartFile multipartFile;


}