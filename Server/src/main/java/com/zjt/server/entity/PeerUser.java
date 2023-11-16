package com.zjt.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName peer_user
 */
@TableName(value ="peer_user")
@Data
public class PeerUser implements Serializable {
    /**
     * 
     */
    @TableId(value = "NO")
    private String no;

    /**
     * 
     */
    @TableField(value = "NAME")
    private String name;

    /**
     * 
     */
    @TableField(value = "ROLE")
    private Integer role;

    /**
     * 
     */
    @TableField(value = "PASSWORD")
    private String password;

    /**
     * 
     */
    @TableField(value = "PHONE")
    private String phone;

    /**
     * 
     */
    @TableField(value = "EMAIL")
    private String email;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     *所属角色
     */
    @TableField(exist = false)
    private String role_cn;


    /**
     * 前端传过来的旧密码
     */
    @TableField(exist = false)
    private String oldPassword;

    /**
     * 前端传过来的新密码
     */
    @TableField(exist = false)
    private String newPassword;

    /**
     * 辨识添加还是修改的id记号
     */
    @TableField(exist = false)
    private int id = 1;
}