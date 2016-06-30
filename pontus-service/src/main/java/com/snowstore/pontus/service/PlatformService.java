package com.snowstore.pontus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.snowstore.pontus.domain.Platform;
import com.snowstore.pontus.repository.PlatformRepository;

@Service
public class PlatformService {

	@Autowired
	private PlatformRepository platformRepository;

	@Cacheable(value = "platform", key = "#adminUserId")
	public List<Platform> getAdminUserPlatformService(Long adminUserId) {
		List<Platform> platformList = platformRepository.getAdminUserPlatform(adminUserId);
		return platformList;
	}
}
