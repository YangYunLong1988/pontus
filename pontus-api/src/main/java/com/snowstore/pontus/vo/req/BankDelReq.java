package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.vo.RequestVo;

public class BankDelReq extends RequestVo {

	@FormParam(value = "bankInfoId")
	@NotBlank
	private String bankInfoId;

	public String getBankInfoId() {
		return bankInfoId;
	}

	public void setBankInfoId(String bankInfoId) {
		this.bankInfoId = bankInfoId;
	}


}
