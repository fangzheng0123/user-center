package com.fz.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fz.usercenter.model.domain.User;
import com.fz.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.fz.usercenter.content.UserContent.REDIS_KEY_TITLE;

/**
 * @Author fang
 * @Date 2025/2/21 10:20
 * @注释  定时执行同步redis任务
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private List<Long> mainUserList = Arrays.asList(2L);

    @Scheduled(cron = "0 30 10 * * *")
    public void doCacheRecommendUser(){
        for (Long userId : mainUserList){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
            try {
                redisTemplate.opsForValue().set((String.format(REDIS_KEY_TITLE,userId)),userPage,60000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.info("redis set error",e);
            }
        }
    }

}
