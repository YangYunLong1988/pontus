package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;
import org.hibernate.validator.constraints.NotBlank;
import com.snowstore.pontus.vo.RequestVo;

public class QuoteContractUpdateWrokFLowReq extends RequestVo {

	@FormParam(value = "quoteContractId")
	@NotBlank
	private String quoteContractId;// 合同主键

	public String getQuoteContractId() {
		return quoteContractId;
	}

	public void setQuoteContractId(String quoteContractId) {
		this.quoteContractId = quoteContractId;
	}

	@Override
	public String toString() {
		return "QuoteContractUpdateWrokFLowReq [quoteContractId=" + quoteContractId + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
