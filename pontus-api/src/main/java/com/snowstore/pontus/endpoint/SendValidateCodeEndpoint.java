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

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snowstore.pontus.domain.ValidateCode.Scene;
import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.ValidateCode.System;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.service.ValidateCodeService;
import com.snowstore.pontus.service.vo.SendValidateCodeForm;
import com.snowstore.pontus.vo.ResponseVo;
import com.snowstore.pontus.vo.req.SendValidateCodeReq;

@Path("/getCode")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SendValidateCodeEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendValidateCodeEndpoint.class);

	@Autowired
	ValidateCodeService validateCodeService;

	@Autowired
	CustomerService customerService;

	@Autowired
	Mapper dozerBeanMapper;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "发送短信验证码")
	public ResponseVo sendValidateCode(@BeanParam @Valid SendValidateCodeReq req,@QueryParam("accessToken") String accessToken) {
		LOGGER.debug("发送短信验证码请求参数【" + req + "】");
		try {
			SendValidateCodeForm form = new SendValidateCodeForm();
			dozerBeanMapper.map(req, form);
			form.setScene(req.getType());
			form.setSystem(System.APP);
			if (req.getType() == Scene.SIGN){
				form.setCustomerId(Long.valueOf(customerService.getCustomerId(accessToken)));
			}
			validateCodeService.sendValidateCode(form);
			ResponseVo resp = new ResponseVo();
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("获取验证码失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("获取验证码失败", e);
			throw new PontusServiceException("获取验证码失败", e);
		}
	}
}
