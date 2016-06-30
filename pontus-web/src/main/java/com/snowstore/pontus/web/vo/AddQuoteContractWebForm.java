package com.snowstore.pontus.web.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class AddQuoteContractWebForm {

	private String contractCode;// 合同编号

	private String platform;// 购买平台，资产来源

	private BigDecimal principal;// 投资本金
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date contractEndDate;// 合同到期日，债权到期日

	private String productName;// 产品名称

	private String term;// 产品期限

	private BigDecimal yearIrr;// 年化收益率

	private String paybackType;// 还款方式

	private BigDecimal unPayedInterest;// 未还利息

	private BigDecimal totalAmount;// 本息和

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

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public BigDecimal getUnPayedInterest() {
		return unPayedInterest;
	}

	public void setUnPayedInterest(BigDecimal unPayedInterest) {
		this.unPayedInterest = unPayedInterest;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public BigDecimal getYearIrr() {
		return yearIrr;
	}

	public void setYearIrr(BigDecimal yearIrr) {
		this.yearIrr = yearIrr;
	}

	public String getPaybackType() {
		return paybackType;
	}

	public void setPaybackType(String paybackType) {
		this.paybackType = paybackType;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

}
