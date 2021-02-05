package com.helun.menu.redis;

import redis.clients.jedis.Jedis;

public class RedisTest {
		public static void main(String[] args) {
			connection() ;
		}
		
		public static Jedis connection() {
			Jedis jedis = new Jedis("192.168.1.28",33553) ;
			jedis.auth("123456") ;
			System.out.println(jedis.ping());
			return jedis ;
		}
		
		
}
