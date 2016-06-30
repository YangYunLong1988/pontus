package com.snowstore.pontus.web.vo;

import org.hibernate.validator.constraints.NotEmpty;

import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Customer;

public class IdentifyAuthenticationForm {
	@NotEmpty(message = "姓名不能为空")
	private String idCardName; // 身份证姓名

	@NotEmpty(message = "身份证号不能为空")
	private String idCardAccount; // 身份证号

	private String name; // 银行名称

	@NotEmpty(message = "账号不能为空")
	private String account; // 账号

	private String province; // 分支行省份(企业结算需要)

	private String city; // 分支行城市(企业结算需要)

	private String subbranch; // 支行，eg.南京东路支行

	private String residenceProvince; // 居住地省份

	private String residenceCity; // 居住地城市

	private String idCardPositive;// 身份证正面

	private String idCardOpposite; // 身份证反面

	private String personalCard;// 名片

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getSubbranch() {
		return subbranch;
	}

	public void setSubbranch(String subbranch) {
		this.subbranch = subbranch;
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

	public String getIdCardPositive() {
		return idCardPositive;
	}

	public void setIdCardPositive(String idCardPositive) {
		this.idCardPositive = idCardPositive;
	}

	public String getIdCardOpposite() {
		return idCardOpposite;
	}

	public void setIdCardOpposite(String idCardOpposite) {
		this.idCardOpposite = idCardOpposite;
	}

	public String getPersonalCard() {
		return personalCard;
	}

	public void setPersonalCard(String personalCard) {
		this.personalCard = personalCard;
	}

	public void copyAuthenFormToCustomer(Customer customer) {
		customer.setIdCardName(this.getIdCardName());
		customer.setIdCardAccount(this.getIdCardAccount());
		customer.setResidenceProvince(this.getResidenceProvince());
		customer.setResidenceCity(this.getResidenceCity());
	}

	public void copyAuthenFormToBankInfo(BankInfo bank) {
		bank.setName(this.getName());
		bank.setAccount(this.getAccount());
		bank.setProvince(this.getProvince());
		bank.setCity(this.getCity());
		bank.setSubbranch(this.getSubbranch());
	}
}
