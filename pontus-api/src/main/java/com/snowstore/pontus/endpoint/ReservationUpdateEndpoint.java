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
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.service.QuoteContractService;
import com.snowstore.pontus.service.vo.AddQuoteContractForm;
import com.snowstore.pontus.vo.req.ReservationUpdateReq;
import com.snowstore.pontus.vo.resp.AddReservationUpdateResp;

/**
 * 合同更新
 * @Project: pontus-api
 * @Author zy
 * @Company: 
 * @Create Time: 2016年5月8日 下午1:38:41
 */
@Path("/reservationUpdate")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ReservationUpdateEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReservationUpdateEndpoint.class);

	@Autowired
	QuoteContractService quoteContractService;
	@Autowired
	CustomerService customerService;

	@Autowired
	Mapper dozerBeanMapper;

	@Autowired
	FastDateFormat fastDateFormat;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "挂牌合同更新")
	public AddReservationUpdateResp addQuoteContract(@BeanParam @Valid ReservationUpdateReq req) {
		LOGGER.debug("更新合同请求参数【" + req + "】");
		try {
			AddReservationUpdateResp resp = new AddReservationUpdateResp();
			
			AddQuoteContractForm form = new AddQuoteContractForm();
			dozerBeanMapper.map(req, form);
			form.setContractEndDate(fastDateFormat.parse(req.getEndDate()));
			resp.setReservationId(quoteContractService.updateQuoteContract(form,Long.valueOf(customerService.getCustomerId(req.getAccessToken())),Long.valueOf(req.getQuoteContractId())));
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("更新合同信息失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("更新合同信息失败", e);
			throw new PontusServiceException("更新合同信息失败", e);
		}
	}
}
