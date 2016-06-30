package com.snowstore.pontus.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * 项目名称：pontus-domain 类名称：ThirdPartyAuthenticationLog 类描述：第三方认证日志 创建人：admin
 * 创建时间：2016年5月4日 上午10:49:31 修改人：admin 修改时间：2016年5月4日 上午10:49:31 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_tp_auth_log")
@EntityListeners(AuditingEntityListener.class)
public class ThirdPartyAuthenticationLog extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8502570705075298098L;

	private Date authTime;// 认证时间
	private String authResult;// 认证结果
	private String refuseReason;// 拒绝原因
	private String idCardName;// 身份证姓名
	private String idCardAccount;// 身份证号码
	@ManyToOne
	@JoinColumn(name = "BANK_ID")
	private BankInfo bankInfo;// 银行卡
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;// 会员

	public Date getAuthTime() {
		return authTime;
	}

	public void setAuthTime(Date authTime) {
		this.authTime = authTime;
	}

	public String getAuthResult() {
		return authResult;
	}

	public void setAuthResult(String authResult) {
		this.authResult = authResult;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public String getIdCardName() {
		return idCardName;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public String getIdCardAccount() {
		return idCardAccount;
	}

	public void setIdCardAccount(String idCardAccount) {
		this.idCardAccount = idCardAccount;
	}


	public BankInfo getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(BankInfo bankInfo) {
		this.bankInfo = bankInfo;
	}


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
