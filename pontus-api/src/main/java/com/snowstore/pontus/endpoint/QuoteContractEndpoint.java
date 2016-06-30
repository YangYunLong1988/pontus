package com.snowstore.pontus.endpoint;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.time.FastDateFormat;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.service.QuoteContractService;
import com.snowstore.pontus.vo.req.QuoteContractUpdateWrokFLowReq;
import com.snowstore.pontus.vo.resp.QuoteContractUpdateWrokFLowResp;

@Path("/reservation")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class QuoteContractEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteContractEndpoint.class);

	@Autowired
	QuoteContractService quoteContractService;
	@Autowired
	CustomerService customerService;

	@Autowired
	Mapper dozerBeanMapper;

	@Autowired
	FastDateFormat fastDateFormat;

	@Path("/updateWorkFLow")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "更新合同状态为待审核")
	public QuoteContractUpdateWrokFLowResp addQuoteContract(@BeanParam @Valid QuoteContractUpdateWrokFLowReq req) {
		LOGGER.debug("更新合同状态请求参数【" + req + "】");
		QuoteContractUpdateWrokFLowResp resp = new QuoteContractUpdateWrokFLowResp();
		try {
			quoteContractService.updateWorkFlow(Long.valueOf(req.getQuoteContractId()), Enums.QuoteContractWorkFlow.PENDING.getValue());
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("更新合同状态失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("更新合同状态败", e);
			throw new PontusServiceException("更新合同状态失败", e);
		}
	}
}
