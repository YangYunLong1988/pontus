/**
 *  Copyright 2016-2026 the original author or authors.
 *  @author xiaodong-java
 *  @date 2016年4月13日
 */
package com.snowstore.pontus.service.vo;

import java.math.BigDecimal;
import java.util.List;

public class SimpleTransferQueryForm {
	private String state;// 有效，无效
	private String platform;
	private BigDecimal discountRateBegin;
	private BigDecimal discountRateEnd;
	private Integer remainingTimeBegin;
	private Integer remainingTimeEnd;
	private List<String> workFlow;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getDiscountRateBegin() {
		return discountRateBegin;
	}

	public void setDiscountRateBegin(BigDecimal discountRateBegin) {
		this.discountRateBegin = discountRateBegin;
	}

	public BigDecimal getDiscountRateEnd() {
		return discountRateEnd;
	}

	public void setDiscountRateEnd(BigDecimal discountRateEnd) {
		this.discountRateEnd = discountRateEnd;
	}

	public Integer getRemainingTimeBegin() {
		return remainingTimeBegin;
	}

	public void setRemainingTimeBegin(Integer remainingTimeBegin) {
		this.remainingTimeBegin = remainingTimeBegin;
	}

	public Integer getRemainingTimeEnd() {
		return remainingTimeEnd;
	}

	public void setRemainingTimeEnd(Integer remainingTimeEnd) {
		this.remainingTimeEnd = remainingTimeEnd;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public List<String> getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(List<String> workFlow) {
		this.workFlow = workFlow;
	}

}
