package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.vo.RequestVo;

public class BankSubbranchReq extends RequestVo {

	@FormParam(value = "bankName")
	@NotBlank
	private String bankName;// 银行名称


	@FormParam(value = "province")
	@NotBlank
	private String province;// 分支行省份(企业结算需要)

	@FormParam(value = "city")
	@NotBlank
	private String city;// 分支行城市(企业结算需要)

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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
		return "BankSubbranchReq [bankName=" + bankName + ", province=" + province + ", city=" + city + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
