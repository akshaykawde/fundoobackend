package com.bridgelabz.fundoo.utility;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil<T> {
	@SuppressWarnings("unused")
	private RedisTemplate<String, Object> redisTemplate;
//	private HashOperations<String, Object, String> hashOperation;
	private ValueOperations<String, T> ValueOperations;

	@Autowired
	RedisUtil(RedisTemplate<String, T> redisTemplate) {
		this.ValueOperations = redisTemplate.opsForValue();
	}

	public void putMap(String redisKey, T value) {
		ValueOperations.set(redisKey, value);
	}

	public T getMap(String redisKey) {
		return ValueOperations.get(redisKey);
	}

	public void getValue(String token) {

		
	}

}
