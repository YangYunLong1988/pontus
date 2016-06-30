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
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.service.vo.ResetPasswordForm;
import com.snowstore.pontus.vo.ResponseVo;
import com.snowstore.pontus.vo.req.ResetPasswordReq;

/**
 * 重置密码
 * 
 */
@Path("/resetPassword")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ResetPasswordEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordEndpoint.class);
	@Autowired
	public CustomerService customerService;

	@Autowired
	Mapper dozerBeanMapper;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "重置密码")
	public ResponseVo init(@BeanParam @Valid ResetPasswordReq req) {
		try {
			ResponseVo resp = new ResponseVo();
			ResetPasswordForm form = new ResetPasswordForm();
			dozerBeanMapper.map(req, form);
			form.setCustomerId(Long.valueOf(customerService.getCustomerId(req.getAccessToken())));
			customerService.resetPassword(form);
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("重置密码异常", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("重置密码异常", e);
			throw new PontusServiceException("重置密码异常", e);
		}

	}

}
