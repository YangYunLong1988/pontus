package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.vo.RequestVo;

public class ResetPasswordReq extends RequestVo {

	@FormParam(value = "oldPassword")
	@NotBlank
	private String oldPassword;

	@FormParam(value = "newPassword")
	@NotBlank
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
