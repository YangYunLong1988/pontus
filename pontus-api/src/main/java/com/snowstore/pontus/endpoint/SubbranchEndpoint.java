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
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.repository.BranchBankRepository;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.vo.req.BankSubbranchReq;
import com.snowstore.pontus.vo.resp.BankSubbranchResp;


/**
 * 获取支行
 * @Project: pontus-api
 * @Author zy
 * @Company: 
 * @Create Time: 2016年5月5日 下午1:14:39
 */
@Path("/bank")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SubbranchEndpoint {

	@Autowired
	private BranchBankRepository branchBankRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(SubbranchEndpoint.class);
			
	@Path("/subbranch")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "查询分支行")
	public BankSubbranchResp init(@BeanParam BankSubbranchReq req) {
		LOGGER.debug("获取支行请求参数【" + req + "】");
		BankSubbranchResp resp = new BankSubbranchResp();
		try{
			resp.setBranchBankList(branchBankRepository.findBranchBanks(req.getProvince(), req.getCity(), req.getBankName(), Enums.BranchBankState.NORMAL.getValue()));
		} catch (Exception e) {
			LOGGER.error("获取支行异常", e);
			throw new PontusServiceException("获取支行异常", e);
		}
		return resp;
	}
	
	
}
