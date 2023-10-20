package com.stars.springbootinit.manager;

import com.stars.springbootinit.common.ErrorCode;
import com.stars.springbootinit.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Prometheus
 * @description 专门提供 RedisLimiter 限流基础服务的
 * @createDate 2023/9/14
 */
@Component
public class RedisLimiterManager {
    @Resource
    private RedissonClient redissonClient;

    /**
     *  限流操作
     * @param key 区分不同的限流器，比如不同的用户 id 应该分别统计
     */
    public void doRedisLimit(String key){
        //创建一个名称为限流器，每秒𡥥访问2次
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL,100,2, RateIntervalUnit.MINUTES);
        //每当一个操作来了后，请求一个令牌
        boolean result = rateLimiter.tryAcquire(1);
        if (!result){
            throw  new BusinessException(ErrorCode.TOO_MANY_REQUEST);

        }
    }

}
