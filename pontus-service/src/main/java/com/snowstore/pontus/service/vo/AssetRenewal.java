package com.snowstore.pontus.service.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AssetRenewal {

	private Long assetId;
	private Long quoteContractId;
	private String platform;// 购买平台
	private String contractCode;// 合同编号
	private String productName;// 产品名称
	private BigDecimal yearIrr;// 年化收益率
	private Integer term;// 期限
	private String paybackType;// 还款方式
	private String ensureType;// 保障方式
	private String investorName;// 姓名
	private String certiType;// 证件类型
	private String certiNo;// 证件号码
	private String mobile;// 手机号码
	private String bankName;// 银行名称
	private String bankNo;// 银行卡号
	private String province;// 所在省份
	private String city;// 所在城市
	private String branchName;// 开户行
	private BigDecimal principal;// 投资本金
	private BigDecimal unPayedInterest;// 未还利息
	private BigDecimal totalAmount;// 本息和
	private BigDecimal profit;// 收益
	private Boolean signState;// 展期协议签署状态

	private String endDate;// 合同到期日
	private String status;

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getYearIrr() {
		return yearIrr;
	}

	public void setYearIrr(BigDecimal yearIrr) {
		this.yearIrr = yearIrr;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public String getPaybackType() {
		return paybackType;
	}

	public void setPaybackType(String paybackType) {
		this.paybackType = paybackType;
	}

	public String getEnsureType() {
		return ensureType;
	}

	public void setEnsureType(String ensureType) {
		this.ensureType = ensureType;
	}

	public String getInvestorName() {
		return investorName;
	}

	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}

	public String getCertiType() {
		return certiType;
	}

	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	public String getCertiNo() {
		return certiNo;
	}

	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	@JsonSerialize(using = BigDecimalSerializer.class)
	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	@JsonSerialize(using = BigDecimalSerializer.class)
	public BigDecimal getUnPayedInterest() {
		return unPayedInterest;
	}

	public void setUnPayedInterest(BigDecimal unPayedInterest) {
		this.unPayedInterest = unPayedInterest;
	}

	@JsonSerialize(using = BigDecimalSerializer.class)
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@JsonSerialize(using = BigDecimalSerializer.class)
	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public Long getQuoteContractId() {
		return quoteContractId;
	}

	public void setQuoteContractId(Long quoteContractId) {
		this.quoteContractId = quoteContractId;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Boolean getSignState() {
		return signState;
	}

	public void setSignState(Boolean signState) {
		this.signState = signState;
	}

}
