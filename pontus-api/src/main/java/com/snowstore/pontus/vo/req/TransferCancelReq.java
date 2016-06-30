package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;
import org.hibernate.validator.constraints.NotBlank;
import com.snowstore.pontus.vo.RequestVo;

public class TransferCancelReq extends RequestVo {

	@FormParam(value = "transferId")
	@NotBlank
	private String transferId;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	@Override
	public String toString() {
		return "TransferCancelReq [transferId=" + transferId + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
