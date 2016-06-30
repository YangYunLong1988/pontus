package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;
import org.hibernate.validator.constraints.NotBlank;
import com.snowstore.pontus.vo.RequestVo;

public class AssigneeSubmitReq extends RequestVo {

	@FormParam(value = "transferId")
	@NotBlank
	private String transferId;

	@FormParam(value = "bankInfoId")
	@NotBlank
	private String bankInfoId;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getBankInfoId() {
		return bankInfoId;
	}

	public void setBankInfoId(String bankInfoId) {
		this.bankInfoId = bankInfoId;
	}

	@Override
	public String toString() {
		return "AssigneeSubmitReq [transferId=" + transferId + ", bankInfoId=" + bankInfoId + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
