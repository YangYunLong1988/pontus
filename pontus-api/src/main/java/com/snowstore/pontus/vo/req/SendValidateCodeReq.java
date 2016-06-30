package com.snowstore.pontus.vo.req;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.domain.ValidateCode.Scene;
import com.snowstore.pontus.vo.EnumInclude;

public class SendValidateCodeReq {

	@FormParam(value = "phone")
	@NotBlank
	private String phone;

	@FormParam(value = "type")
	@NotNull
	@EnumInclude(scene = { Scene.REGISTER, Scene.RETRIEVE, Scene.SIGN })
	private Scene type;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Scene getType() {
		return type;
	}

	public void setType(Scene type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SendValidateCodeReq [phone=" + phone + ", type=" + type + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
