package com.fz.usercenter.content;

/**
 * @Author fang
 * @Date 2025/2/1 15:15
 * @注释
 */
public interface UserContent {

    /*
    session 键
     */
    String USER_LOGIN_STATE = "userLoginState";
    /*
    用户权限管理信息，0-表示普通用户 1-表示管理员
     */
    int DEFAULT_USER = 0;
    int ADMIN_USER = 1;
    String DEFAULT_AVATAR_URL = "https://img1.baidu.com/it/u=3999177305,1743000370&fm=253&fmt=auto&app=138&f=JPEG?w=859&h=800";
}
