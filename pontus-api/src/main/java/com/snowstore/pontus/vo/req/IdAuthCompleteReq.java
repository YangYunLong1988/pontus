package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.vo.RequestVo;

public class IdAuthCompleteReq extends RequestVo {

	@FormParam(value = "account")
	@NotBlank
	private String account;// 账号

	@FormParam(value = "bankName")
	@NotBlank
	private String bankName;// 银行名称

	@FormParam(value = "province")
	@NotBlank
	private String province;// 分支行省份(企业结算需要)

	@FormParam(value = "city")
	@NotBlank
	private String city;// 分支行城市(企业结算需要)

	@FormParam(value = "subbranch")
	@NotBlank
	private String subbranch;// 支行，eg.南京东路支行

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	@Override
	public String toString() {
		return "IdAuthCompleteReq [account=" + account + ", bankName=" + bankName + ", province=" + province + ", city=" + city + ", subbranch=" + subbranch + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

}
