package com.fz.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fz.usercenter.common.BaseResponse;
import com.fz.usercenter.common.ErrorCode;
import com.fz.usercenter.common.ResultUtils;
import com.fz.usercenter.exception.BusinessException;
import com.fz.usercenter.model.domain.User;
import com.fz.usercenter.service.UserService;
import com.fz.usercenter.mapper.UserMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.fz.usercenter.content.UserContent.*;

/**
 * @author fang
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2025-01-26 19:40:24
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String SALT = "FZFZ";

    @Override
    public long userRegister(String userAccount, String UserPassword, String checkPassword,String planetCode) {
//        对输入信息进行判断
        if (StringUtils.isAnyBlank(userAccount, UserPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"输入参数不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号不能小于4");
        }
        if (UserPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度不能小于8");
        }
//        对编程用户编号进行长度校验
        if (planetCode.length() > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程用户编号不能大于5");
        }
//        正则表达式来判断是否包含特殊字符
        String regex = "[!@#\\$%\\^&*()_+\\-=\\{\\}\\[\\]:\";'\\,\\.\\?/\\\\\\|]";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能包含特殊字符");
        }
//        校验二次密码是否一致
        if (!UserPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"二次输入的密码不一致");
        }
//        对密码进行加密
//        加盐
        String encPassword = DigestUtils.md5DigestAsHex((SALT + UserPassword).getBytes());
//        查询数据库中是否已经存在用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(userQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"已经存在相同用户，不能重复创建");
        }
        //        查询数据库中是否已经存在编程用户编号
        userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(userQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"已经存在相同编程用户编号，不能重复创建");
        }
//        加入到数据库中
        User user = new User();
        user.setUseraccount(userAccount);
        user.setUserpassword(encPassword);
        user.setPlanetcode(planetCode);
        user.setAvatarurl(DEFAULT_AVATAR_URL);
        user.setUsername(userAccount);
        this.save(user);
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号或密码不能为空");
        }
        if (userAccount.length() < 4 || userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号长度要大于4且密码长度大于8");
        }
        //        正则表达式来判断是否包含特殊字符
        String regex = "[!@#\\$%\\^&*()_+\\-=\\{\\}\\[\\]:\";'\\,\\.\\?/\\\\\\|]";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号不能包含特殊字符");
        }
        //        对密码进行加密
//        加盐
        String encPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userPassword", encPassword);
        userQueryWrapper.eq("userAccount",userAccount);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null){
            log.info("user login failed,userAccount cannot match userPassword!");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码错误");
        }
        User safetyUser = safetyUser(user);
//        传递session
        httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);

        return safetyUser;
    }


    /**
     * 用户脱敏
     * @param user 原始用户信息
     * @return 脱敏用户信息
     */
    @Override
    public User safetyUser(User user){
        User saftyUser = new User();
        if (user == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户信息为空");
        }
        saftyUser.setId(user.getId());
        saftyUser.setUsername(user.getUsername());
        saftyUser.setUseraccount(user.getUseraccount());
        saftyUser.setAvatarurl(user.getAvatarurl());
        saftyUser.setGender(user.getGender());
        saftyUser.setPhone(user.getPhone());
        saftyUser.setEmail(user.getEmail());
        saftyUser.setUserrole(user.getUserrole());
        saftyUser.setProfile(user.getProfile());
        saftyUser.setTags(user.getTags());
        saftyUser.setPlanetcode(user.getPlanetcode());
        saftyUser.setUserstatus(user.getUserstatus());
        saftyUser.setCreatetime(user.getCreatetime());
        return saftyUser;
    }

    /**
     *   用户注销
      * @param request 请求返回消息
     * @return
     */
    @Override
    public BaseResponse<String> logoutUser(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return ResultUtils.success("用户注销成功");
    }

    /**
     * 查询标签
     * @param tagNameList  标签集合
     * @return
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameList) {

        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //        第一种方法
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        for (String tagName : tagNameList) {
//            queryWrapper.like("tags",tagName);
//        }
//        List<User> userList = userMapper.selectList(queryWrapper);
//        return userList.stream().map(this::safetyUser).collect(Collectors.toList());

//        第二种方法 查内存
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
//        在内存中判断是否有包含要求的标签
        return userList.stream().filter(user -> {
            String tagsStr = user.getTags();  //这里是json类型的字符串
            if (StringUtils.isBlank(tagsStr)){
                return false;
            }
//            下面作用就是将tagsStr json字符串反序列化为对象
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {
            }.getType());
//            对数据的一个判断，如果为空，就给他赋值一个空值
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::safetyUser).collect(Collectors.toList());
    }


    /**
     * 获取登录用户信息
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object user = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return (User) user;
    }
    /**
     *  根据请求信息判断用户是不是管理员
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserrole() == ADMIN_USER;
    }

    @Override
    public boolean isAdmin(User loginUser) {
        return loginUser != null && loginUser.getUserrole() == ADMIN_USER;
    }

    /**
     *
     * @param user 用户修改的信息
     * @param loginUser 修改用户信息的用户
     * @return
     */
    @Override
    public int updateUser(User user, User loginUser) {
        long userId = loginUser.getId();
//        获取用户的id信息
        if (userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!isAdmin(loginUser) && userId != user.getId()){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
//        如果传递的数据只有id变，其他没变就报错
        if (user.getAvatarurl() == null && user.getUsername()==null&& user.getGender()==null&& user.getPhone()==null&& user.getEmail()==null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
//        再查一次数据库中有没有该用户
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }


    /**
     * 将用户信息存储到redis服务中
     * @param request
     * @return
     */
    @Override
    public Page<User> addRedisWithPage(HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        String redisKey = String.format(REDIS_KEY_TITLE, loginUser.getId());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Page<User> userPage = (Page<User>)valueOperations.get(redisKey);
        if (userPage != null){
            return userPage;
        }
        return null;
    }
}
