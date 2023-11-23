package com.zjt.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName sc
 */
@TableName(value ="sc")
@Data
public class Sc implements Serializable {
    /**
     * 
     */
    @TableId(value = "cno")
    private String cno;

    /**
     * 
     */
    @TableId(value = "sno")
    private String sno;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}