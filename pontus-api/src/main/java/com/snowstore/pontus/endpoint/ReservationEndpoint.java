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

import java.math.BigDecimal;

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
import com.snowstore.pontus.vo.req.AddReservationReq;
import com.snowstore.pontus.vo.resp.AddReservationResp;

@Path("/reservation")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ReservationEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReservationEndpoint.class);

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
	@UserLog(remark = "新增挂牌合同")
	public AddReservationResp addQuoteContract(@BeanParam @Valid AddReservationReq addReservationReq) {
		try {
			AddReservationResp resp = new AddReservationResp();
			AddQuoteContractForm addQuoteContractForm = new AddQuoteContractForm();
			dozerBeanMapper.map(addReservationReq, addQuoteContractForm);
			if(null != addReservationReq.getYearIrr()) {
				addQuoteContractForm.setYearIrr(new BigDecimal(addReservationReq.getYearIrr()));
			}
			addQuoteContractForm.setContractEndDate(fastDateFormat.parse(addReservationReq.getEndDate()));
			addQuoteContractForm.setCustomerId(Long.valueOf(customerService.getCustomerId(addReservationReq.getAccessToken())));
			resp.setReservationId(quoteContractService.saveQuoteContract(addQuoteContractForm));
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("新增合同信息失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("新增合同信息失败", e);
			throw new PontusServiceException("新增合同信息失败", e);
		}
	}
}
