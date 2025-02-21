package com.fz.usercenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fz.usercenter.common.BaseResponse;
import com.fz.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.fz.usercenter.content.UserContent.ADMIN_USER;
import static com.fz.usercenter.content.UserContent.USER_LOGIN_STATE;

/**
* @author fang
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-01-26 19:40:24
*/
public interface UserService extends IService<User> {

    /**
     * 注册
     *
     * @param userAccount   用户账号
     * @param password      用户密码
     * @param checkPassword 确认密码
     * @param planetCode    用户编程编号
     * @return 返回值
     */
    long userRegister(String userAccount, String password, String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount        用户账号
     * @param userPassword       用户密码
     * @param httpServletRequest 请求
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

    /**
     * 用户脱敏
     *
     * @param user 用户对象信息
     * @return 脱敏用户信息
     */
    User safetyUser(User user);

    BaseResponse<String> logoutUser(HttpServletRequest request);

    /**
     * 查询标签
     *
     * @param tagNameList 标签集合
     * @return
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     * 获取登录用户信息
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 根据请求信息判断用户是不是管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     *  根据登录用户判断是否为管理员
     * @param   loginUser
     * @return
     */
    boolean isAdmin(User loginUser);

    /**
     * 更新用户
     * @param user 用户修改的信息
     * @param loginUser 修改用户信息的用户
     * @return 修改行数
     */
    int updateUser(User user, User loginUser);

    /**
     * 将用户信息存储到redis服务中
     * @param request
     * @return
     */
    Page<User> addRedisWithPage(HttpServletRequest request);
}