package com.fz.usercenter.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author fang
 * @Date 2025/2/21 14:03
 * @注释
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedissionConfig {
    private String port;
    private String host;
    @Bean
    public RedissonClient redissonClient(){
//        创建配置
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer().setAddress(redisAddress).setDatabase(1);
//        创建实例
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
