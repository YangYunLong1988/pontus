package com.snowstore.pontus.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.snowstore.pontus.enums.Enums.OriginalContractState;

/**
 *
 * 项目名称：pontus-domain 类名称：OriginalContract 类描述：原始合同表 创建人：admin 创建时间：2016年5月4日
 * 下午12:24:51 修改人：admin 修改时间：2016年5月4日 下午12:24:51 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_original_contract")
@EntityListeners(AuditingEntityListener.class)
public class OriginalContract extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -43862316195464992L;

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
	private String branchName;// 开户行.
	private BigDecimal principal;// 投资本金
	private BigDecimal unPayedInterest;// 未还利息
	private BigDecimal totalAmount;// 本息和
	private BigDecimal profit;// 收益
	@DateTimeFormat(pattern = "yyyy-MM-dd")  
	private Date contractEndDate;// 合同到期日
	private String status = OriginalContractState.VALID.getValue();
	@DateTimeFormat(pattern = "yyyy-MM-dd")  
	private Date interestStartDate;// 起息日期
	
	private String workFlow;//工作流 初始，已绑定

	@OneToOne
	@JoinColumn(name = "QUOTE_ID")
	private QuoteContract quoteContract;

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

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public BigDecimal getUnPayedInterest() {
		return unPayedInterest;
	}

	public void setUnPayedInterest(BigDecimal unPayedInterest) {
		this.unPayedInterest = unPayedInterest;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public QuoteContract getQuoteContract() {
		return quoteContract;
	}

	public void setQuoteContract(QuoteContract quoteContract) {
		this.quoteContract = quoteContract;
	}

	public Date getInterestStartDate() {
		return interestStartDate;
	}

	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

}
