package org.finance.infrastructure.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

/**
 * @author jiangbangfa
 */
@EnableCaching
@Configuration
public class SpringCacheConfig extends CachingConfigurerSupport {

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder("spring:");
            sb.append(target.getClass().getName()).append(":").append(method.getName());
            for (Object obj : params) {
                if (obj != null) {
                    sb.append(":").append(obj);
                }
            }
            return sb.toString();
        };
    }

    @Bean
    public LettucePoolingClientConfiguration getPoolConfig() {
        return LettucePoolingClientConfiguration.builder().build();
    }

   /* @Bean
    public RedisCacheManager cacheManager(ObjectMapper objectMapper, RedisConnectionFactory redisConnectionFactory) {
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        Map<String, RedisCacheConfiguration> cacheConfigMap = new HashMap<>();
        cacheConfigMap.put(CacheValueConst.CACHE_OF_1m, this.getRedisCacheConfigurationWithTtl(1 * 60, objectMapper));
        cacheConfigMap.put(CacheValueConst.CACHE_OF_10m, this.getRedisCacheConfigurationWithTtl(10 * 60, objectMapper));
        cacheConfigMap.put(CacheValueConst.CACHE_OF_30m, this.getRedisCacheConfigurationWithTtl(30 * 60, objectMapper));
        cacheConfigMap.put(CacheValueConst.CACHE_OF_1h, this.getRedisCacheConfigurationWithTtl(60 * 60, objectMapper));
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                this.getRedisCacheConfigurationWithTtl( 600, objectMapper), // 默认策略，未配置的 key 会使用这个
                cacheConfigMap // 指定 key 策略
        );
    }*/

}
