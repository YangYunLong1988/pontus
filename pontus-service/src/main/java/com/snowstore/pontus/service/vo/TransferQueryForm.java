/**
 *  Copyright 2016-2026 the original author or authors.
 *  @author xiaodong-java
 *  @date 2016年4月13日
 */
package com.snowstore.pontus.service.vo;

import java.util.Calendar;
import java.util.Date;

import com.snowstore.pontus.service.common.Calendars;

public class TransferQueryForm {
	private String state;// 有效，无效
	private String query;// 转让人姓名,转让人电话,转让编号
	private String status;// 交易时状态
	private String applyTime;// 申请时间范围
	private Date applyFrom;
	private Date applyTo;

	public TransferQueryForm() {

	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
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

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		String[] time = applyTime.split("~");
		this.applyFrom = Calendars.StringToDate(time[0]);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Calendars.StringToDate(time[1]));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		this.applyTo = calendar.getTime();
		this.applyTime = applyTime;
	}

	public Date getApplyFrom() {
		return applyFrom;
	}

	public void setApplyFrom(Date applyFrom) {
		this.applyFrom = applyFrom;
	}

	public Date getApplyTo() {
		return applyTo;
	}

	public void setApplyTo(Date applyTo) {
		this.applyTo = applyTo;
	}

}
