package com.snowstore.pontus.service.vo;

import java.util.Calendar;
import java.util.Date;

import com.snowstore.pontus.service.common.Calendars;

public class ContractQueryForm {
	private String productResource;// 产品来源
	private String status;// 状态
	private String query;// 产品名称，证件号码，拥有人
	private String queryAgreement;// 展期协议查询:合同来源,协议名称,协议编号,拥有人
	private String applyDate;//
	private Date applyDateFrom;
	private Date applyDateTo;
	private String workFlow;
	private String paybackType;
	private Long customerId;

	public String getProductResource() {
		if ("全部".equals(productResource)) {
			return null;
		}
		return productResource;
	}

	public void setProductResource(String productResource) {
		this.productResource = productResource;
	}

	public String getStatus() {
		if ("全部".equals(status)) {
			return null;
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQueryAgreement() {
		return queryAgreement;
	}

	public void setQueryAgreement(String queryAgreement) {
		this.queryAgreement = queryAgreement;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		String[] time = applyDate.split("~");
		this.applyDateFrom = Calendars.StringToDate(time[0]);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Calendars.StringToDate(time[1]));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		this.applyDateTo = calendar.getTime();
		this.applyDate = applyDate;
	}

	public Date getApplyDateFrom() {
		return applyDateFrom;
	}

	public void setApplyDateFrom(Date applyDateFrom) {
		this.applyDateFrom = applyDateFrom;
	}

	public Date getApplyDateTo() {
		return applyDateTo;
	}

	public void setApplyDateTo(Date applyDateTo) {
		this.applyDateTo = applyDateTo;
	}

	public String getWorkFlow() {
		if ("全部".equals(workFlow)) {
			return null;
		}
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public String getPaybackType() {
		return paybackType;
	}

	public void setPaybackType(String paybackType) {
		this.paybackType = paybackType;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

}
