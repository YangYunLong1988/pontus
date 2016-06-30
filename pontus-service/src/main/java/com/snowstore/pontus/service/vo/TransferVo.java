package com.snowstore.pontus.service.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.snowstore.pontus.enums.Enums;

public class TransferVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private BigDecimal discountRate;// 折扣率
	private BigDecimal transferedValue;// 挂牌后额转让价值
	private String code;// 转让单编号
	private String workFlow = Enums.TransferFlow.PENDING.getValue();// 转让状态状态
	private Date createdDate;// 创建时间

	// 挂牌合同QuoteContract
	private String contractCode;// 合同编号
	private BigDecimal principal;// 投资本金
	// 用户部分Customer
	private String idCardName;// 转让人
	private String phone;// 转让人电话
	private Date endDate;// 展期到期日期，合同到期日期+10个月
	// 预约部分
	private int assigneeCount;// 预约人数

	// 原始合同id
	private Long originalContractId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public BigDecimal getTransferedValue() {
		return transferedValue;
	}

	public void setTransferedValue(BigDecimal transferedValue) {
		this.transferedValue = transferedValue;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public String getIdCardName() {
		return idCardName;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAssigneeCount() {
		return assigneeCount;
	}

	public void setAssigneeCount(int assigneeCount) {
		this.assigneeCount = assigneeCount;
	}

	public Long getOriginalContractId() {
		return originalContractId;
	}

	public void setOriginalContractId(Long originalContractId) {
		this.originalContractId = originalContractId;
	}

}
