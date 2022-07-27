package com.distributed.cache.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.distributed.cache.service.CacheService;

import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
@Configuration
@EnableCaching
public class APIConfig extends CachingConfigurerSupport {

	@Bean
	public net.sf.ehcache.CacheManager ehCacheManager() {
		CacheConfiguration autoSuggestCache = new CacheConfiguration();
		autoSuggestCache.setName("user-cache");
		autoSuggestCache.setMemoryStoreEvictionPolicy("LRU");
		autoSuggestCache.setMaxEntriesLocalHeap(3000);
		autoSuggestCache.setTimeToLiveSeconds(86400);
		net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
		config.addCache(autoSuggestCache);
		return net.sf.ehcache.CacheManager.newInstance(config);
	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheManager());
	}
	
	 @Bean("cacheKeyGenerator")
	    public KeyGenerator keyGenerator() {
	        return new CacheService();
	    }
}