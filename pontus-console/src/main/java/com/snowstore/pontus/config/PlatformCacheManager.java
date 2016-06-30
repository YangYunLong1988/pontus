package com.snowstore.pontus.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class PlatformCacheManager {

	@Bean
	public CacheManager getCacheManager() {
		return new ConcurrentMapCacheManager("platform");
	}
}
