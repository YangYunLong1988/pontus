package com.snowstore.pontus.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.pontus.domain.Platform;
import com.snowstore.pontus.repository.AdminUserRepository;
import com.snowstore.pontus.service.userDetails.UserDetailsImpl;

@Service
@Transactional
public class AdminUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserService.class);
	
	@Autowired
	private AdminUserRepository adminUserRepository;
	
	public UserDetailsImpl getUserDetails() {
		Authentication authen = SecurityContextHolder.getContext().getAuthentication();
		if ("anonymousUser".equals(authen.getPrincipal())) {
			LOGGER.info("匿名用户");
			return null;
		} else if(!(authen.getPrincipal() instanceof UserDetailsImpl)){
			return null;
		}else{
			return (UserDetailsImpl) authen.getPrincipal();
		}
	}
	
	public List<String> getUserPlatforms(){
		List<String> list = new ArrayList<String>();
		UserDetailsImpl userDetails = getUserDetails();
		if(null == userDetails){
			return list;
		}
		Set<Platform> platforms =  adminUserRepository.findOne(userDetails.getId()).getPlatFormSet();
		
		for (Platform platform : platforms) {
			list.add(platform.getPlatFormName());
		}
		return list;
	}
	
	public Boolean checkPlatforms(Set<String> set){
		List<String> platforms = getUserPlatforms();
		int orginSize = set.size()+platforms.size();
		set.addAll(platforms);
		if(orginSize>set.size()){
			return true;
		}else{
			return false;
		}
	}
}
