package com.zjt.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName homeworkrevise
 */
@TableName(value ="homeworkrevise")
@Data
public class Homeworkrevise implements Serializable {
    /**
     * 作业ID
     */
    @TableId(value = "HID")
    private Integer hid;

    /**
     * 批改人Sno
     */
    @TableId(value = "Reviser")
    private String reviser;

    /**
     * 提交人Sno
     */
    @TableId(value = "CommitSno")
    private String commitsno;

    /**
     * 评语
     */
    @TableField(value = "Remark")
    private String remark;

    /**
     * 得分
     */
    @TableField(value = "Score")
    private Integer score;

    /**
     * 批改状态（Y/N）
     */
    @TableField(value = "Status")
    private String status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private int no;
}