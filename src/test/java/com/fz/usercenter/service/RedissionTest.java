package com.fz.usercenter.service;

import com.fz.usercenter.config.RedissionConfig;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author fang
 * @Date 2025/2/21 14:08
 * @注释
 */
@SpringBootTest
public class RedissionTest {
    @Resource
    private RedissonClient redissonClient;
    @Test
    void test(){
        RList<String> rList = redissonClient.getList("user-list");
//        rList.add("1234");
        rList.remove(0);
    }
}
