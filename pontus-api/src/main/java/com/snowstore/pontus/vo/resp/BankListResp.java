package com.snowstore.pontus.vo.resp;

import java.util.List;

import com.snowstore.pontus.vo.ResponseVo;

public class BankListResp extends ResponseVo {

	private List<BankInfoResp> bankInfoRespList;

	public List<BankInfoResp> getBankInfoRespList() {
		return bankInfoRespList;
	}

	public void setBankInfoRespList(List<BankInfoResp> bankInfoRespList) {
		this.bankInfoRespList = bankInfoRespList;
	}

}
