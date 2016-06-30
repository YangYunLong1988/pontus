package com.snowstore.pontus.service.userDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.service.CustomerService;

@Transactional
@Service
public class WebCustomDetailsService implements UserDetailsService {

	@Autowired
	private CustomerService customerService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer customer = customerService.findByPhone(username);
		if (null == customer) {
			throw new UsernameNotFoundException("用户不存在");
		}
		return new WebUserDetailsImpl(customer.getId(), customer.getPhone(), customer.getPassword());
	}

	public Long getCustomerId() {
		Authentication currentuser = SecurityContextHolder.getContext().getAuthentication();
		if (currentuser == null || currentuser.getPrincipal().equals("anonymousUser")) {
			return null;
		}
		return ((WebUserDetailsImpl) currentuser.getPrincipal()).getId();
	}

}
