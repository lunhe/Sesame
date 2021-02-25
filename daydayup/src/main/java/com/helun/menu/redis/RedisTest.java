package com.helun.menu.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.Protocol;

public class RedisTest {
		public static void main(String[] args) {
			connection() ;
		}
		
		public static void connection() {
			RedisTemplate rt = functionDomainRedisTemplate( ) ;
			rt.afterPropertiesSet();
			
			System.out.println(rt.getClientList()) ;
		}
		
		
	    public static RedisTemplate<String, Object> functionDomainRedisTemplate() {
	        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
	        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("192.168.3.247",
	    			Protocol.DEFAULT_PORT);
	        RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(config) ;
	        redisConnectionFactory.getConnection() ;
	        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
	        return redisTemplate;
	    }

	    /**
	     * 设置数据存入 redis 的序列化方式
	     *
	     * @param redisTemplate
	     * @param factory
	     */
	    private static void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {
	        redisTemplate.setKeySerializer(new StringRedisSerializer());
	        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
	        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
	        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
	        redisTemplate.setConnectionFactory(factory);
	    }
		
}
