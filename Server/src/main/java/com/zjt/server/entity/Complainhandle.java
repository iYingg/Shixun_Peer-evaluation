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
 * @TableName complainhandle
 */
@TableName(value ="complainhandle")
@Data
public class Complainhandle implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "HandleID", type = IdType.AUTO)
    private Integer handleid;

    /**
     * 申诉的ID
     */
    @TableField(value = "ComplainID")
    private Integer complainid;

    /**
     * 回复内容
     */
    @TableField(value = "HandleContent")
    private String handlecontent;

    /**
     * 回复时间
     */
    @TableField(value = "HandleDate")
    private Date handledate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}