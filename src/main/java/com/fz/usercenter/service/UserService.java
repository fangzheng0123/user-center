package com.fz.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fz.usercenter.common.BaseResponse;
import com.fz.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author fang
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-01-26 19:40:24
*/
public interface UserService extends IService<User> {

    /**
     * 注册
     *
     * @param userAccount 用户账号
     * @param password  用户密码
     * @param checkPassword  确认密码
     * @param planetCode 用户编程编号
     * @return  返回值
     */
    long userRegister(String userAccount,String password,String checkPassword,String planetCode);

    /**
     * 用户登录
     * @param userAccount  用户账号
     * @param userPassword  用户密码
     * @param httpServletRequest  请求
     * @return  用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

    /**
     *  用户脱敏
     * @param user  用户对象信息
     * @return  脱敏用户信息
     */
    User safetyUser(User user);

    BaseResponse<String> logoutUser(HttpServletRequest request);
}
