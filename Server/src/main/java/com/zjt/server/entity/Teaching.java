package com.zjt.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName teaching
 */
@TableName(value ="teaching")
@Data
public class Teaching implements Serializable {
    /**
     * 教师号
     */
    @TableId(value = "tno")
    private String tno;

    /**
     * 课程号
     */
    @TableId(value = "cno")
    private String cno;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}