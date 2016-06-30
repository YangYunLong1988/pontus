package com.snowstore.pontus.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snowstore.pontus.enums.Enums;

/**
 * 
 * 项目名称：pontus-domain 类名称：Customer 类描述：会员表 创建人：admin 创建时间：2016年5月4日 下午12:53:45
 * 修改人：admin 修改时间：2016年5月4日 下午12:53:45 修改备注：
 * 
 * @version
 * 
 */
@Entity
@Table(name = "pontus_customer")
@EntityListeners(AuditingEntityListener.class)
public class Customer extends AbstractEntity implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6398840872607982669L;
	private String username;// 用户名
	private String email;// 邮箱
	private String residenceProvince;// 居住地省份
	private String residenceCity;// 居住地城市
	private String idCardAccount;// 身份证号
	private String idCardName;// 身份证姓名
	private String phone;// 手机号
	private String status = Enums.CustomerState.VALID.getValue();// 状态

	private String account;// 账号
	private String certiType;// 证件类型
	private String telephone;// 电话
	private String password;// 密码
	private String unauthReason;// 未通过认证原因
	private String workFlow;// 工作流
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@OrderBy(clause = "defaulted DESC, created_date DESC")
	private Set<BankInfo> bankSet = new HashSet<>();// 认证银行卡
	
	@JsonIgnore
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<QuoteContract> quoteContractSet = new HashSet<QuoteContract>();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResidenceProvince() {
		return residenceProvince;
	}

	public void setResidenceProvince(String residenceProvince) {
		this.residenceProvince = residenceProvince;
	}

	public String getResidenceCity() {
		return residenceCity;
	}

	public void setResidenceCity(String residenceCity) {
		this.residenceCity = residenceCity;
	}

	public String getIdCardAccount() {
		return idCardAccount;
	}

	public void setIdCardAccount(String idCardAccount) {
		this.idCardAccount = idCardAccount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCertiType() {
		return certiType;
	}

	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUnauthReason() {
		return unauthReason;
	}

	public void setUnauthReason(String unauthReason) {
		this.unauthReason = unauthReason;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	/**
	 * 获取默认银行卡
	 * 
	 * @return
	 */
	@Transient
	public BankInfo getBankInfo() {
		for (BankInfo bank : bankSet) {
			if (bank.getDefaulted()) {
				return bank;
			}
		}
		return null;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdCardName() {
		return idCardName;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<BankInfo> getBankSet() {
		return bankSet;
	}

	public void setBankSet(Set<BankInfo> bankSet) {
		this.bankSet = bankSet;
	}
	
	public Set<QuoteContract> getQuoteContractSet() {
		return quoteContractSet;
	}

	public void setQuoteContractSet(Set<QuoteContract> quoteContractSet) {
		this.quoteContractSet = quoteContractSet;
	}

	@Transient
	public String getAdress() {
		Set<String> spec = new HashSet<>();
		spec.add("北京");
		spec.add("天津");
		spec.add("上海");
		spec.add("重庆");
		spec.add("台湾");
		spec.add("澳门");
		spec.add("香港");
		if (spec.contains(residenceProvince)) {
			return residenceProvince;
		}
		if (null == residenceProvince) {
			return "";
		}
		return residenceProvince + residenceCity;
	}

}
