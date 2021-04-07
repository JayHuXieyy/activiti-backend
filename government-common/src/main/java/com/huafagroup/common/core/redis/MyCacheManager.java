package com.huafagroup.common.core.redis;

import com.huafagroup.common.utils.MyCacheNameConstant;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jay
 */
@Configuration
@ComponentScan
@EnableCaching
public class MyCacheManager {
    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();
        // 创建一个缓存，过期时间ttl为12分钟，同时最长空闲时maxIdleTime为12分钟。
        config.put(MyCacheNameConstant.THIRD_PARTY_GUIDE_DETAILS, new CacheConfig(24 * 60 * 60 * 1000, 24 * 60 * 60 * 1000));
        config.put(MyCacheNameConstant.THIRD_PARTY_GUIDE_LIST, new CacheConfig(24 * 60 * 60 * 1000, 24 * 60 * 60 * 1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
