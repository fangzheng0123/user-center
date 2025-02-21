package com.fz.usercenter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.fz.usercenter.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

/**
 * @Author fang
 * @Date 2025/2/20 16:32
 * @注释
 */
@SpringBootTest
public class InsertUserTest {

    @Resource
    private UserService userService;

    @Test
    public void insertUser() {
        StopWatch stopWatch = new StopWatch();
        final int RESULT_NUM = 100000;
        System.out.println("aaaaaa");
        stopWatch.start();

        ArrayList<User> userList = new ArrayList<>();
        for (int i = 0; i < RESULT_NUM; i++) {
            User user = new User();
            user.setUsername("假用户" + i);
            user.setUserpassword("11111111");
            user.setAvatarurl("https://img1.baidu.com/it/u=3999177305,1743000370&fm=253&fmt=auto&app=138&f=JPEG?w=859&h=800");
            user.setUseraccount("user");
            user.setGender(0);
            user.setPhone("1234567");
            user.setEmail("1234@qq.com");
            user.setCreatetime(new Date());
            user.setPlanetcode("11" + i);
            user.setTags("[]");
            user.setProfile("");

            userList.add(user);
        }
        userService.saveBatch(userList, 10000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());

    }

    @Test
    public void doInsertUser() {
        StopWatch stopWatch = new StopWatch();
        final int RESULT_NUM = 100000;
        int batchSize = 5000;
        stopWatch.start();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(60, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < 20; i++) {
            ArrayList<User> userList = new ArrayList<>();
            while (true) {
                j++;
                User user = new User();
                user.setUsername("假用户" + i);
                user.setUserpassword("11111111");
                user.setAvatarurl("https://img1.baidu.com/it/u=3999177305,1743000370&fm=253&fmt=auto&app=138&f=JPEG?w=859&h=800");
                user.setUseraccount("user");
                user.setGender(0);
                user.setPhone("1234567");
                user.setEmail("1234@qq.com");
                user.setCreatetime(new Date());
                user.setPlanetcode("11" + i);
                user.setTags("[]");
                user.setProfile("");
                userList.add(user);
                if (j % batchSize == 0) {
                    break;
                }
            }
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread().getName());
                userService.saveBatch(userList, batchSize);
            },threadPoolExecutor);
            futureList.add(future);
        }

        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();


        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());

    }

}
