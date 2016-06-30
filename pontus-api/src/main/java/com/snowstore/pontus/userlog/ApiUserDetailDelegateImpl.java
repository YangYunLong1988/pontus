package com.snowstore.pontus.userlog;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snowstore.log.service.UserDetailDelegate;
import com.snowstore.log.vo.UserInfo;
import com.snowstore.pontus.audit.ApiSession;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.repository.CustomerRepository;

@Component
public class ApiUserDetailDelegateImpl implements UserDetailDelegate<Customer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserDetailDelegateImpl.class);

	private Map<String, UserInfo> cacheUser = new HashMap<String, UserInfo>();

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserInfo getUserInfo() {
		UserInfo userInfo = null;
		String customerId = ApiSession.getCustomerId();
		if (null != customerId) {
			// 取缓存
			userInfo = cacheUser.get(customerId);
			if (null == userInfo) {
				Customer customer = customerRepository.findOne(Long.valueOf(customerId));
				if (null != customer) {
					userInfo = new UserInfo();
					userInfo.setUserId(customer.getId());
					userInfo.setUserName(customer.getUsername());
					// 设置缓存
					cacheUser.put(customerId, userInfo);
				} else {
					LOGGER.warn("根据customerId【" + customerId + "】查询实体为空");
					userInfo = createDefaultUserInfo();
				}
			}
		} else {
			LOGGER.warn("没用从ThreadLocal中取到customerId");
			userInfo = createDefaultUserInfo();
		}
		return userInfo;
	}

	private UserInfo createDefaultUserInfo() {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(0L);
		return userInfo;
	}

}
