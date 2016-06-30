package com.snowstore.pontus.vo.resp;

import java.math.BigDecimal;

import com.snowstore.pontus.vo.ResponseVo;

public class AccountAmountResp extends ResponseVo {

	private BigDecimal investAmount;

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

}
