package com.snowstore.pontus.vo.resp;

import java.math.BigDecimal;

public class AssigneeRecordResp {

	private Long assigneeId;//转让记录 主键
	private Long transferId;
	private Long quoteContractId;
	private String contractCode;// 合同编号
	private BigDecimal principal;// 投资本金
	private BigDecimal creditorRight; // 债权价值
	private String assigneeWorkFlow;// 受让状态
	private BigDecimal discountRate;// 折扣率
	private BigDecimal expectedReturn;// 预期收益
	private BigDecimal actualPrice;// 受让价格
	private String remainderDate;// 剩余期限
	private int bespeakCount;// 预约人数

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public BigDecimal getCreditorRight() {
		return creditorRight;
	}

	public void setCreditorRight(BigDecimal creditorRight) {
		this.creditorRight = creditorRight;
	}

	public BigDecimal getExpectedReturn() {
		return expectedReturn;
	}

	public void setExpectedReturn(BigDecimal expectedReturn) {
		this.expectedReturn = expectedReturn;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public BigDecimal getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getRemainderDate() {
		return remainderDate;
	}

	public void setRemainderDate(String remainderDate) {
		this.remainderDate = remainderDate;
	}

	public int getBespeakCount() {
		return bespeakCount;
	}

	public void setBespeakCount(int bespeakCount) {
		this.bespeakCount = bespeakCount;
	}

	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	public String getAssigneeWorkFlow() {
		return assigneeWorkFlow;
	}

	public void setAssigneeWorkFlow(String assigneeWorkFlow) {
		this.assigneeWorkFlow = assigneeWorkFlow;
	}

	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	public Long getQuoteContractId() {
		return quoteContractId;
	}

	public void setQuoteContractId(Long quoteContractId) {
		this.quoteContractId = quoteContractId;
	}

}
