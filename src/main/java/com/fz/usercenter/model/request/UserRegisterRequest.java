package com.fz.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author fang
 * @Date 2025/1/30 10:18
 * @注释  用户注册前端请求
 */

@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 5374525137615954517L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
