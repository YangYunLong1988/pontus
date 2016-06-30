package com.snowstore.pontus.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snowstore.pontus.enums.Enums.QuoteContracState;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;

/**
 * 
 * 项目名称：pontus-domain 类名称：QuoteContract 类描述：挂牌合同表 创建人：admin 创建时间：2016年5月4日
 * 上午11:14:20 修改人：admin 修改时间：2016年5月4日 上午11:14:20 修改备注：
 * 
 * @version
 * 
 */
@Entity
@Table(name = "pontus_quote_contract")
@EntityListeners(AuditingEntityListener.class)
public class QuoteContract extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 320030194423904936L;

	private String contractCode;// 合同编号
	private String platform;// 购买平台，资产来源
	private BigDecimal principal;// 投资本金
	private Date contractEndDate;// 合同到期日，债权到期日
	private BigDecimal unPayedInterest;// 未还利息
	private BigDecimal totalAmount;// 本息和
	private BigDecimal profit;// 收益
	private String productName;// 产品名称
	private Integer term;// 产品期限
	private BigDecimal yearIrr;// 年化收益率
	private String paybackType;// 还款方式
	private String ensureType;// 担保方式
	private String workFlow = QuoteContractWorkFlow.NEW.getValue();// 工作流
	private String auditRefuseReason;// 审核拒绝原因
	private Boolean signState = Boolean.FALSE;// 签署状态
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "quoteContract")
	private RenewalAgreement renewalAgreement;
	
	private String state = QuoteContracState.VALID.getValue();

	public Boolean getSignState() {
		return signState;
	}

	public void setSignState(Boolean signState) {
		this.signState = signState;
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

	@OneToOne
	@JoinColumn(name = "CONTRACT_ID")
	@JsonIgnore
	private OriginalContract originalContract;// 关联的原始合同

	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;// 客户

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
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

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public String getAuditRefuseReason() {
		return auditRefuseReason;
	}

	public void setAuditRefuseReason(String auditRefuseReason) {
		this.auditRefuseReason = auditRefuseReason;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public OriginalContract getOriginalContract() {
		return originalContract;
	}

	public void setOriginalContract(OriginalContract originalContract) {
		this.originalContract = originalContract;
	}

	public RenewalAgreement getRenewalAgreement() {
		return renewalAgreement;
	}

	public void setRenewalAgreement(RenewalAgreement renewalAgreement) {
		this.renewalAgreement = renewalAgreement;
	}

	/**
	 * 获取剩余时间
	 */
	@Transient
	public long getRemainingTime() {
		if (null == contractEndDate) {
			return 0;
		}
		int days = Days.daysBetween(new DateTime().millisOfDay().withMinimumValue(), new DateTime(contractEndDate).millisOfDay().withMinimumValue()).getDays();
		return days < 0 ? 0 : days;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
