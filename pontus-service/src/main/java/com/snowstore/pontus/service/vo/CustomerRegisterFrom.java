package com.snowstore.pontus.service.vo;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.snowstore.pontus.domain.ValidateCode.Scene;

public class CustomerRegisterFrom {

	@NotEmpty(message = "手机号码不能为空")
	@Digits(fraction = 0, integer = 11, message = "手机号为11位")
	@Pattern(regexp = "1[3|4|5|7|8]\\d{9}", message = "手机号11位")
	private String phone; // 手机号码

	@NotEmpty(message = "密码不能为空")
	@Pattern(regexp = "(.{6,16})", message = "6-16位字符且为字母+数字的组合")
	private String password; // 密码

	private String email; // 常用电子邮箱

	private String username;

	private String validateCode;

	private Scene scene;
	private com.snowstore.pontus.domain.ValidateCode.System system;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public com.snowstore.pontus.domain.ValidateCode.System getSystem() {
		return system;
	}

	public void setSystem(com.snowstore.pontus.domain.ValidateCode.System system) {
		this.system = system;
	}

}
