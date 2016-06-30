/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.snowstore.pontus.endpoint;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.vo.ResponseVo;
import com.snowstore.pontus.vo.req.LogoutReq;

/**
 * @author sm
 *
 */
@Path("/logout")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class LogoutEndpoint {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogoutEndpoint.class);
	@Autowired
	CustomerService customerService;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "用户登出")
	public ResponseVo login(@BeanParam LogoutReq req) {
		try {
			ResponseVo resp = new ResponseVo();
			// 查询用户
			String customerId = customerService.getCustomerId(req.getAccessToken());
			Customer customer = customerService.get(Long.valueOf(customerId));
			if (null == customer) {
				throw new Exception("无效用户");
			}
			customerService.logout(req.getAccessToken());
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("退出失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("退出失败", e);
			throw new PontusServiceException("退出失败", e);
		}
	}
}
