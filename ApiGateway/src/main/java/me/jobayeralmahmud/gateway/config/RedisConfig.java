package me.jobayeralmahmud.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

     @Bean
     public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
         RedisCacheConfiguration defaultCacheConfiguration =
                 RedisCacheConfiguration.defaultCacheConfig()
                         .serializeKeysWith(RedisSerializationContext.SerializationPair
                                 .fromSerializer(new StringRedisSerializer()))
                         .serializeValuesWith(RedisSerializationContext.SerializationPair
                                 .fromSerializer(RedisSerializer.json()))
                         .entryTtl(Duration.ofMinutes(20));

         Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
         cacheConfigurations.put("products",  defaultCacheConfiguration.entryTtl(Duration.ofHours(10)));

         return RedisCacheManager.builder(redisConnectionFactory)
                 .cacheDefaults(defaultCacheConfiguration)
                 .withInitialCacheConfigurations(cacheConfigurations)
                 .build();

     }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJacksonJsonRedisSerializer(new ObjectMapper()));

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJacksonJsonRedisSerializer(new ObjectMapper()));
        return template;
    }

}