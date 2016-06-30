package com.snowstore.pontus.vo.resp;

import java.math.BigDecimal;

public class SpecialContractResp {

	private Long quoteContractId;
	private String contractCode;// 合同编号
	private String platform;// 购买平台
	private BigDecimal principal;// 投资本金
	private String contractEndDate;// 合同到期日

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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public String getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
}
