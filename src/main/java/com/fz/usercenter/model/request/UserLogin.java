package com.fz.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author fang
 * @Date 2025/1/30 10:34
 * @注释
 */
@Data
public class UserLogin implements Serializable {
    private static final long serialVersionUID = -8938996924602679739L;
    private String userAccount;
    private String userPassword;
}
