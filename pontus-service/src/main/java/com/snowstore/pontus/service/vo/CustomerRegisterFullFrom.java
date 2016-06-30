package com.snowstore.pontus.service.vo;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

public class CustomerRegisterFullFrom extends CustomerRegisterFrom {
	@NotEmpty(message = "确认密码不能为空")
	@Pattern(regexp = "(.{6,16})", message = "6-16位字符且为字母+数字的组合")
	private String password2;

	@NotEmpty(message = "验证码不能为空")
	private String imgCode;

	@NotEmpty(message = "手机验证码不能为空")
	@Pattern(regexp = "\\d{6}", message = "验证码为六位数字")
	private String code;

	private String confirmed;

	private boolean isAgree = false; // 是否同意协议

	private boolean isLegalPWD = false; // 密码是否合法

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getImgCode() {
		return imgCode;
	}

	public void setImgCode(String imgCode) {
		this.imgCode = imgCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}

	public boolean getIsAgree() {
		if (this.confirmed != null && this.confirmed != "") {
			this.isAgree = true;
		}
		return this.isAgree;
	}

	public boolean getIsLegalPWD() {
		if (this.password2 != null && super.getPassword() != null && this.password2.equals(super.getPassword())) {
			this.isLegalPWD = true;
		}
		return this.isLegalPWD;
	}
}
