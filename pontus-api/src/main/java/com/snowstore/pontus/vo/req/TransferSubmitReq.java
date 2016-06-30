package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;
import org.hibernate.validator.constraints.NotBlank;
import com.snowstore.pontus.vo.RequestVo;

public class TransferSubmitReq extends RequestVo {

	@FormParam(value = "quoteContractId")
	@NotBlank
	private String quoteContractId;

	@FormParam(value = "discountRate")
	@NotBlank
	private String discountRate;// 折扣率

	public String getQuoteContractId() {
		return quoteContractId;
	}

	public void setQuoteContractId(String quoteContractId) {
		this.quoteContractId = quoteContractId;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	@Override
	public String toString() {
		return "QueryTransferPriceReq [quoteContractId=" + quoteContractId + ", discountRate=" + discountRate + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
}
