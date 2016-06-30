package com.snowstore.pontus.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * 项目名称：pontus-domain 类名称：BranchBank 类描述：银行分支信息表 创建人：admin 创建时间：2016年5月6日
 * 上午11:04:01 修改人：admin 修改时间：2016年5月6日 上午11:04:01 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_branch_bank")
@EntityListeners(AuditingEntityListener.class)
public class BranchBank extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2822415619487512452L;
	// 省份
	private String province;
	// 城市
	private String city;
	// 城市代码
	private String cityCode;
	// 银行
	private String bankName;
	// 分行
	private String branchBankName;
	// 状态
	private String status;

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

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchBankName() {
		return branchBankName;
	}

	public void setBranchBankName(String branchBankName) {
		this.branchBankName = branchBankName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
