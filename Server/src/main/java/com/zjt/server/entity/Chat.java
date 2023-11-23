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
 * @TableName chat
 */
@TableName(value ="chat")
@Data
public class Chat implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "ChatID", type = IdType.AUTO)
    private Integer chatid;

    /**
     * 发送人id
     */
    @TableField(value = "Sender")
    private String sender;

    /**
     * 内容
     */
    @TableField(value = "Content")
    private String content;

    /**
     * 发送时间
     */
    @TableField(value = "Date")
    private Date date;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}