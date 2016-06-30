package com.snowstore.pontus.service.vo;

import java.math.BigDecimal;
import java.util.Date;

public class CreateQuoteContractForm {
	private String contractCode;// 合同编号
	private String investPlatform;// 购买平台，资产来源
	private BigDecimal investPprinciple;// 投资本金
	private Date contractEndDate;// 合同到期日，债权到期日
	private BigDecimal unPayedInterest;// 未还利息
	private BigDecimal profit;// 收益
	private String prodcutName;// 产品名称
	private String productTerm;// 产品期限
	private BigDecimal annualRate;// 年化收益率
	private String repaymentMode;// 还款方式
	private String guaranteeMode;// 担保方式

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getInvestPlatform() {
		return investPlatform;
	}

	public void setInvestPlatform(String investPlatform) {
		this.investPlatform = investPlatform;
	}

	public BigDecimal getInvestPprinciple() {
		return investPprinciple;
	}

	public void setInvestPprinciple(BigDecimal investPprinciple) {
		this.investPprinciple = investPprinciple;
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

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public String getProdcutName() {
		return prodcutName;
	}

	public void setProdcutName(String prodcutName) {
		this.prodcutName = prodcutName;
	}

	public String getProductTerm() {
		return productTerm;
	}

	public void setProductTerm(String productTerm) {
		this.productTerm = productTerm;
	}

	public BigDecimal getAnnualRate() {
		return annualRate;
	}

	public void setAnnualRate(BigDecimal annualRate) {
		this.annualRate = annualRate;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}

	public String getGuaranteeMode() {
		return guaranteeMode;
	}

	public void setGuaranteeMode(String guaranteeMode) {
		this.guaranteeMode = guaranteeMode;
	}

}
