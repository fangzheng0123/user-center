package com.fz.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fz.usercenter.common.BaseResponse;
import com.fz.usercenter.common.ErrorCode;
import com.fz.usercenter.common.ResultUtils;
import com.fz.usercenter.exception.BusinessException;
import com.fz.usercenter.model.domain.User;
import com.fz.usercenter.model.request.UserLogin;
import com.fz.usercenter.model.request.UserRegisterRequest;
import com.fz.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.fz.usercenter.content.UserContent.ADMIN_USER;
import static com.fz.usercenter.content.UserContent.USER_LOGIN_STATE;

/**
 * @Author fang
 * @Date 2025/1/30 10:16
 * @注释
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
//    用户注册
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
//        判断
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
//        再次进行判断
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long userId = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        return ResultUtils.success(userId);
    }

//    用户登录
    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserLogin userLogin , HttpServletRequest request){
        //        判断
        if (userLogin == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userLogin.getUserAccount();
        String userPassword = userLogin.getUserPassword();
//        再次进行判断
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

//    用户注销，登出
    @PostMapping("/logout")
    public BaseResponse<String> logoutUser(HttpServletRequest request){
        if (request == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userService.logoutUser(request);
    }

//    用户的登录态
    @GetMapping("/current")
    public BaseResponse<User> getCurrent(HttpServletRequest request){
//        从session中获取用户信息
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
//      从数据库中再次获取用户信息
//        TODO  还有用户的状态检查信息需要再次校验
        User userServiceById = userService.getById(user.getId());
//        用户信息脱敏
        User user1 = userService.safetyUser(userServiceById);
        return ResultUtils.success(user1);
    }


//    用户遍历
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> list = userService.list(queryWrapper);

//        使用数据流的方式对数据进行脱敏处理
        List<User> collect = list.stream().map(user -> userService.safetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(collect);

    }

//    用户删除
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 判断用户是不是管理员
     */
    public boolean isAdmin(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserrole() == ADMIN_USER;
    }
}

