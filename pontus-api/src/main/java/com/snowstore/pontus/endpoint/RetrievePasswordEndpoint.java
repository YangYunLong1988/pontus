package com.snowstore.pontus.endpoint;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.ValidateCode.Scene;
import com.snowstore.pontus.domain.ValidateCode.System;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.service.vo.RetrievePasswordForm;
import com.snowstore.pontus.vo.ResponseVo;
import com.snowstore.pontus.vo.req.RetrievePasswordReq;

/**
 * 重置密码
 * 
 */
@Path("/retrieve")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class RetrievePasswordEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(RetrievePasswordEndpoint.class);
	@Autowired
	private CustomerService customerService;

	@Autowired
	Mapper dozerBeanMapper;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "找回密码")
	public ResponseVo retrievePassword(@BeanParam @Valid RetrievePasswordReq req) {
		try {
			RetrievePasswordForm form = new RetrievePasswordForm();
			dozerBeanMapper.map(req, form);
			form.setScene(Scene.RETRIEVE);
			form.setSystem(System.APP);
			customerService.retrievePassword(form);
			ResponseVo resp = new ResponseVo();
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("找回密码异常", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("找回密码异常", e);
			throw new PontusServiceException("找回密码异常", e);
		}

	}

}
