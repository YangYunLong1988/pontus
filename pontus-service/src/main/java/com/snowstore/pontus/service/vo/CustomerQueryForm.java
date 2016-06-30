package com.snowstore.pontus.service.vo;

import java.util.Calendar;
import java.util.Date;

import com.snowstore.pontus.service.common.Calendars;

public class CustomerQueryForm {
	private String workFlow;
	private String registTime;//注册时间范围 
	private String query;
	private Date registFrom;
	private Date registTo;
	private String status;

	public String getWorkFlow() {
		if("全部".equals(workFlow)){
			return null;
		}
		return workFlow;
	}
	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}
	public String getStatus() {
		if("全部".equals(status)){
			return null;
		}
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRegistTime() {
		return registTime;
	}
	public void setRegistTime(String registTime) {
		String[] time = registTime.split("~");
		this.registFrom = Calendars.StringToDate(time[0]);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Calendars.StringToDate(time[1]));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		this.registTo = calendar.getTime();
		this.registTime = registTime;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Date getRegistFrom() {
		return registFrom;
	}
	public void setRegistFrom(Date registFrom) {
		this.registFrom = registFrom;
	}
	public Date getRegistTo() {
		return registTo;
	}
	public void setRegistTo(Date registTo) {
		this.registTo = registTo;
	}
}
