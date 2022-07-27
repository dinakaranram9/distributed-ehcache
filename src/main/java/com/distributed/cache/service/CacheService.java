package com.distributed.cache.service;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;

import com.distributed.cache.entity.Cache;
import com.distributed.cache.repository.CacheRepository;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CacheService implements KeyGenerator {

	@Autowired
	CacheRepository cacheRepository;

	@Override
	public Object generate(Object target, Method method, Object... params) {
		try {
			// Get cache name
			Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
			if (targetMethod != null && targetMethod.getAnnotation(Cacheable.class) != null) {
				String cacheName = target.getClass().getMethod(method.getName(), method.getParameterTypes())
						.getAnnotation(Cacheable.class).value()[0];
				log.info("Cache name {}", cacheName);
				Cache c = cacheRepository.findById(cacheName).get();
				return String.format("%s_%s", cacheName, c.getVersion());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}
	
	public Cache addCache(Cache cache) {
		cache.setLastUpdatedDateTime(LocalDateTime.now());
		return cacheRepository.save(cache);
	}

}
