package com.snowstore.pontus.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "pontus_work_day")
@EntityListeners(AuditingEntityListener.class)
public class WorkDay extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3598981973008827841L;
	private Date day;// 日期
	private Boolean isWork;// 是否工作日

	@Temporal(TemporalType.DATE)
	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Boolean getIsWork() {
		return isWork;
	}

	public void setIsWork(Boolean isWork) {
		this.isWork = isWork;
	}
}
