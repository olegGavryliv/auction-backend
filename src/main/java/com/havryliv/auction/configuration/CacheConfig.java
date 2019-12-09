package com.havryliv.auction.configuration;

import com.havryliv.auction.exception.RedisCacheErrorHandler;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Data
@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport implements CachingConfigurer {

    @Value("${redis.hostname:localhost}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.timeout:1}")
    private int redisTimeoutInSecs;

    @Value("${redis.socketTimeout:1}")
    private int redisSocketTimeoutInSecs;

    @Value("${redis.ttl:1}")
    private long redisDataTTLInSeconds;

    private Map<String, Long> expirationsCache = new HashMap<>();

    public CacheConfig() {
        expirationsCache.put("Top3Products", 10L); //set time to live for specific cache
        expirationsCache.put("AllProducts", 30L);
    }

    private static RedisCacheConfiguration createCacheConfiguration(Long timeout) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(timeout));
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        log.info("Redis (/Lettuce) configuration enabled. With cache timeout " + redisDataTTLInSeconds + " seconds.");

        final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(redisSocketTimeoutInSecs)).build();

        final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(redisDataTTLInSeconds)).clientOptions(clientOptions).build();
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost, redisPort);

        final LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(serverConfig, clientConfig);

        lettuceConnectionFactory.setValidateConnection(true);
        return lettuceConnectionFactory;

    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
    }


    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(redisDataTTLInSeconds))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java()));


        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        for (Map.Entry<String, Long> cacheNameAndTimeout : expirationsCache.entrySet()) {
            cacheConfigurations.put(cacheNameAndTimeout.getKey(), createCacheConfiguration(cacheNameAndTimeout.getValue()));
        }

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler();
    }
}
