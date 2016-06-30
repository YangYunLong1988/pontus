package com.snowstore.pontus.vo.req;

import javax.validation.constraints.Pattern;
import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.vo.RequestVo;

public class IdAuthInitReq extends RequestVo {

	@FormParam(value = "idCardName")
	@NotBlank
	private String idCardName;

	@FormParam(value = "idCardAccount")
	@NotBlank
	@Pattern(message = "身份证格式不正确", regexp = "[a-zA-Z0-9]{6,30}")
	private String idCardAccount;

	public String getIdCardName() {
		return idCardName;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public String getIdCardAccount() {
		return idCardAccount;
	}

	public void setIdCardAccount(String idCardAccount) {
		this.idCardAccount = idCardAccount;
	}

	@Override
	public String toString() {
		return "IdAuthInitReq [idCardName=" + idCardName + ", idCardAccount=" + idCardAccount + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
