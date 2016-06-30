package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.vo.RequestVo;

public class BankAddReq extends RequestVo {

	@FormParam(value = "userName")
	@NotBlank
	private String userName;

	@FormParam(value = "name")
	@NotBlank
	private String name;// 银行名称

	@FormParam(value = "account")
	@NotBlank
	private String account;// 账号

	@FormParam(value = "subbranch")
	@NotBlank
	private String subbranch;// 支行，eg.南京东路支行

	@FormParam(value = "province")
	@NotBlank
	private String province;// 分支行省份(企业结算需要)

	@FormParam(value = "city")
	@NotBlank
	private String city;// 分支行城市(企业结算需要)

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	@Override
	public String toString() {
		return "BankAddReq [userName=" + userName + ", name=" + name + ", account=" + account + ", subbranch=" + subbranch + ", province=" + province + ", city=" + city + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
