package com.snowstore.pontus.service.vo;

public class BankInfoForm {
	private String name;// 银行名称
	private String account;// 账号
	private String subbranch;// 支行，eg.南京东路支行
	private String province;// 分支行省份(企业结算需要)
	private String city;// 分支行城市(企业结算需要)

	public BankInfoForm() {
		super();
	}

	public BankInfoForm(String name, String account, String subbranch, String province, String city) {
		super();
		this.name = name;
		this.account = account;
		this.subbranch = subbranch;
		this.province = province;
		this.city = city;
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

	public String getSubbranch() {
		return subbranch;
	}

	public void setSubbranch(String subbranch) {
		this.subbranch = subbranch;
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
}
