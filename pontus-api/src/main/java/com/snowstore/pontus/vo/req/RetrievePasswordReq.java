package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

public class RetrievePasswordReq {

	@FormParam(value = "phone")
	@NotBlank
	private String phone;

	@FormParam(value = "code")
	@NotBlank
	private String validateCode;

	@FormParam(value = "newPassword")
	@NotBlank
	private String newPassword;

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
