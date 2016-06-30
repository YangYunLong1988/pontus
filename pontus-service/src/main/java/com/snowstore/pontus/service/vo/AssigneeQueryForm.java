package com.snowstore.pontus.service.vo;

import java.util.Calendar;
import java.util.Date;

import com.snowstore.pontus.service.common.Calendars;

public class AssigneeQueryForm {
	private Long transferId;// 转让单id
	private String state;// 客户登记,回访同意,回访取消,交易完成
	private String query;// 受让人姓名,受让编号,受让人手机号码
	private String applyTime;// 申请时间范围
	private Date applyFrom;
	private Date applyTo;

	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	public String getState() {
		if ("全部".equals(state)) {
			return null;
		}
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
