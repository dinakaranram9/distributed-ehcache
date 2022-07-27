package com.distributed.cache.aspectj;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.distributed.cache.entity.Cache;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Aspect
public class CacheAspectJ {
	@Autowired
	MongoOperations mongoOperations;

	/**
	 * Execute before execution of the method annotated with @CacheEvict
	 *
	 * @param joinPoint AspectJ joinPoint to determine the class and method
	 *                  information
	 */
	@Before("@annotation(org.springframework.cache.annotation.CacheEvict)")
	@Transactional
	public void cacheRefreshHandler(JoinPoint joinPoint) {
		try {
			log.info("Clear cache handler invoked");
			// Get all the cache names from @CacheEvict#value
			long startTime = System.currentTimeMillis();
			Signature signature = joinPoint.getSignature();
			Method[] methods = joinPoint.getTarget().getClass().getMethods();
			for (Method m : methods) {
				if (m.getName().equalsIgnoreCase(signature.getName())) {
					String[] cacheNames = m.getAnnotation(CacheEvict.class).value();
					log.info("Total cache(s) that will be evicted {}", cacheNames.length);
					// Update "last_updated_date_time" column in "cache" table
					for (String c : cacheNames) {
						log.info("Clearing cache {}", c);
						refreshCache(c);
					}
					break;
				}
			}
			log.info("Clear cache handler complete in {} seconds ", ((System.currentTimeMillis() - startTime) / 1000));
		} catch (Exception e) {
			log.error("Error while clearing the cache {}", e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void refreshCache(String cacheName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("cacheName").is(cacheName));
		Cache cache = mongoOperations.findOne(query, Cache.class);
		cache.setLastUpdatedDateTime(LocalDateTime.now());
		cache.setVersion(cache.getVersion()+1);
		mongoOperations.save(cache);
	}
}
