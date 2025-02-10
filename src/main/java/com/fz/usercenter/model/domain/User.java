package com.fz.usercenter.model.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String userpassword;

    /**
     * 用户账号
     */
    private String useraccount;

    /**
     * 用户头像
     */
    private String avatarurl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 管理权限 0 -- 普通用户  1 -- 管理员
     */
    private Integer userrole;

    /**
     * 状态 0 - 正常
     */
    private Integer userstatus;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 
     */
    private Date updatetime;

    /**
     * 编程用户编码
     */
    private String planetcode;

    /**
     * 是否删除
     *
     * @TableLogic  就是表示该字段是逻辑删除字段
     *  在xml文件中也得配置相应的属性信息
     */
    @TableLogic
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}