package com.snowstore.pontus.vo.resp;

import java.math.BigDecimal;

public class AssetTranferResp {

	private Long quoteContractId;
	private String contractCode;// 合同编号
	private String platform;// 购买平台
	private BigDecimal principal;// 投资本金
	private BigDecimal creditorRight; // 债权价值
	private BigDecimal expectedReturn;// 预期收益
	private String paybackType;// 还款方式
	private String isTransfer;// 是否可转让

	public Long getQuoteContractId() {
		return quoteContractId;
	}

	public void setQuoteContractId(Long quoteContractId) {
		this.quoteContractId = quoteContractId;
	}

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

	public String getPaybackType() {
		return paybackType;
	}

	public void setPaybackType(String paybackType) {
		this.paybackType = paybackType;
	}

	public BigDecimal getCreditorRight() {
		return creditorRight;
	}

	public void setCreditorRight(BigDecimal creditorRight) {
		this.creditorRight = creditorRight;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public BigDecimal getExpectedReturn() {
		return expectedReturn;
	}

	public void setExpectedReturn(BigDecimal expectedReturn) {
		this.expectedReturn = expectedReturn;
	}

	public String getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;
	}

}
