package com.zjt.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName course
 */
@TableName(value ="course")
@Data
public class Course implements Serializable {
    /**
     * 课程号
     */
    @TableId(value = "cno")
    private String cno;

    /**
     * 课程名
     */
    @TableField(value = "cname")
    private String cname;

    /**
     * 学时
     */
    @TableField(value = "hours")
    private Integer hours;

    /**
     * 当前用户信息
     */
    @TableField(exist = false)
    private String userNo;

    /**
     * 授课老师号
     */
    @TableField(exist = false)
    private String tno;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}