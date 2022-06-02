package org.finance.infrastructure.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * TODO 缓存service中的相关查询API，以后性能优化在实现
 * @author jiangbangfa
 */
@Component
public class RedisCacheUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


}
