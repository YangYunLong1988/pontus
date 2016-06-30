package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

public class RegisterReq {

	@FormParam(value = "phone")
	@NotBlank
	private String phone;

	@FormParam(value = "password")
	@NotBlank
	private String password;

	@FormParam(value = "code")
	@NotBlank
	private String validateCode;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

}
