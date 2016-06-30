package com.snowstore.pontus.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "pontus_tpp_bank")
@EntityListeners(AuditingEntityListener.class)
public class TppSupportedBank extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6619392171562667425L;

	/** 第三方类型 */
	private String thirdType;

	/** 第三方银行代码 */
	private String bankCode;
	/** 第三方银行名称 */
	private String bankName;
	/** 单笔限额 **/
	private Long singleMaxAmount;
	/** 单天限额 **/
	private Long todayMaxAmount;
	/** 单月限额 **/
	private Long monthMaxAmount;
	/** 账号类型 **/
	private String accountType;
	/** 业务类型 **/
	private String busiType;
	/** 企业账号 **/
	private String account;
	/** 实名验证 **/
	private String specialType;
	/** 备注 **/
	private String remark;
	/** 更新标识 */
	private String updateMark;

	public String getThirdType() {
		return thirdType;
	}

	public void setThirdType(String thirdType) {
		this.thirdType = thirdType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getSingleMaxAmount() {
		return singleMaxAmount;
	}

	public void setSingleMaxAmount(Long singleMaxAmount) {
		this.singleMaxAmount = singleMaxAmount;
	}

	public Long getTodayMaxAmount() {
		return todayMaxAmount;
	}

	public void setTodayMaxAmount(Long todayMaxAmount) {
		this.todayMaxAmount = todayMaxAmount;
	}

	public Long getMonthMaxAmount() {
		return monthMaxAmount;
	}

	public void setMonthMaxAmount(Long monthMaxAmount) {
		this.monthMaxAmount = monthMaxAmount;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSpecialType() {
		return specialType;
	}

	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdateMark() {
		return updateMark;
	}

	public void setUpdateMark(String updateMark) {
		this.updateMark = updateMark;
	}

}
