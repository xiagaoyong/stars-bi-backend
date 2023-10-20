package com.stars.springbootinit.manager;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.security.Key;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Stars
 * @description
 * @createDate 2023/9/14
 */
@SpringBootTest
class RedisLimiterManagerTest {
    @Resource
    private RedisLimiterManager redisLimiterManager;
    //手动创建线程池
//    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
//            2
//            , 10
//            , 2
//            , TimeUnit.SECONDS
//            ,new ArrayBlockingQueue<>(3)
//            ,new ThreadPoolExecutor.DiscardPolicy());


    @Test
    void doRedisLimit() throws InterruptedException {
        String userId = "1";
        for (int i = 0; i < 2; i++) {
            redisLimiterManager.doRedisLimit(userId);
            System.out.println("成功");

        }

        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            redisLimiterManager.doRedisLimit(userId);
            System.out.println("成功2");

        }
    }
}