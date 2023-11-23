package com.zjt.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName complaincommit
 */
@TableName(value ="complaincommit")
@Data
public class Complaincommit implements Serializable {
    /**
     * 申诉ID
     */
    @TableId(value = "ComplainID")
    private Integer complainid;

    /**
     * 提交申诉学生ID
     */
    @TableField(value = "Sno")
    private String sno;

    /**
     * 收到申诉的老师ID
     */
    @TableField(value = "Tno")
    private String tno;

    /**
     * 提交申诉的时间
     */
    @TableField(value = "Date")
    private Date date;

    /**
     * 申诉内容
     */
    @TableField(value = "Content")
    private String content;

    /**
     * 是否处理（Y/N）
     */
    @TableField(value = "Respond")
    private String respond;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}