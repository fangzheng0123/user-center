package com.fz.usercenter.service;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fz.usercenter.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fang
 * @Date 2025/1/26 19:44
 * @注释
 */

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void test(){
        User user = new User();
        user.setUsername("123");
        user.setUserpassword("1231");
        user.setUseraccount("1323");
        user.setAvatarurl("12");
        user.setGender(0);
        user.setPhone("123");
        user.setEmail("123");
        boolean save = userService.save(user);
        Assertions.assertTrue(save);
    }

    @Test
    void userRegister() {

        String userAccount = "fzfz";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String planetCode = "1";
        long l = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1,l);
    }


    @Test
    public void searchUsersByTags(){
        List<String> tagNameList = Arrays.asList("java","python");
        List<User> users = userService.searchUsersByTags(tagNameList);
        assertNotNull(users);
    }

}