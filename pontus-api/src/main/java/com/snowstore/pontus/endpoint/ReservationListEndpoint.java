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

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.snowstore.log.annotation.UserLog;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.service.CustomerService;
import com.snowstore.pontus.service.PontusServiceException;
import com.snowstore.pontus.service.QuoteContractService;
import com.snowstore.pontus.service.common.Calendars;
import com.snowstore.pontus.service.vo.QuoteContractForm;
import com.snowstore.pontus.vo.req.ReservationListReq;
import com.snowstore.pontus.vo.resp.ReservationListResp;
import com.snowstore.pontus.vo.resp.ReservationListResp.ReservationResp;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ReservationListEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReservationListEndpoint.class);

	@Autowired
	QuoteContractService quoteContractService;
	@Autowired
	CustomerService customerService;
	@Autowired
	FastDateFormat fastDateFormat;
	@Autowired
	Mapper dozerBeanMapper;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@UserLog(remark = "查询预约挂牌合同")
	public ReservationListResp queryQuoteContractList(@BeanParam @Valid ReservationListReq reservationListReq) {
		try {
			ReservationListResp resp = new ReservationListResp();
			resp.setReservationList(queryQuoteContractListByApp(reservationListReq));
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("查询预约挂牌失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("查询预约挂牌失败", e);
			throw new PontusServiceException("查询预约挂牌失败", e);
		}
	}

	public List<ReservationResp> queryQuoteContractListByApp(ReservationListReq reservationListReq) {
		try {
			QuoteContractForm form = new QuoteContractForm();
			List<QuoteContractWorkFlow> workFlowList = new ArrayList<QuoteContractWorkFlow>();
			workFlowList.add(Enums.QuoteContractWorkFlow.PENDING);
			workFlowList.add(Enums.QuoteContractWorkFlow.REJECT);
			
			dozerBeanMapper.map(reservationListReq, form);
			form.setCustomerId(Long.valueOf(customerService.getCustomerId(reservationListReq.getAccessToken())));
			form.setWorkFlowList(workFlowList);
			
			Page<QuoteContract> page = quoteContractService.queryQuoteContractList(form);
			List<ReservationResp> resp = new ArrayList<ReservationListResp.ReservationResp>();
			for (QuoteContract quoteContract : page.getContent()) {
				ReservationResp reservationResp = new ReservationResp();
				reservationResp.setEndDate(Calendars.format(quoteContract.getContractEndDate(), Calendars.YYYY_MM_DD));
				dozerBeanMapper.map(quoteContract, reservationResp);
				reservationResp.setReservationId(quoteContract.getId());
				resp.add(reservationResp);
			}
			return resp;
		} catch (PontusServiceException e) {
			LOGGER.error("获取挂牌资产失败", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("获取挂牌资产失败", e);
			throw new PontusServiceException("获取挂牌资产失败", e);
		}
	}
}
