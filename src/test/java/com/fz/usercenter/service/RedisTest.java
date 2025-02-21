package com.fz.usercenter.service;
import java.util.Date;

import com.fz.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @Author fang
 * @Date 2025/2/20 19:20
 * @注释
 */
@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate redisTemplate;
    @Test
    void test(){
//        增
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("fzString","String");
        valueOperations.set("fzInt",1);
        valueOperations.set("fzDouble",2.0);
        User user = new User();
        user.setId(0L);
        user.setUsername("aaaa");
        valueOperations.set("fzUser",user);
//        查
        Object fzString = valueOperations.get("fzString");
        Assertions.assertTrue("String".equals((String) fzString));
        Object fzInt = valueOperations.get("fzInt");
        Assertions.assertTrue(1 == (Integer)fzInt);
        Object fzDouble = valueOperations.get("fzDouble");
        Assertions.assertTrue(2.0 == (Double) fzDouble);
        Object fzUser = valueOperations.get("fzUser");
        System.out.println((User) fzUser);
    }
}
