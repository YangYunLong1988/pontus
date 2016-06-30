package com.snowstore.pontus.vo.resp;

import java.util.List;

import com.snowstore.pontus.vo.ResponseVo;

public class TransferQueryListResp extends ResponseVo {

	private List<TransferQueryResp> transferQueryRespList;

	public List<TransferQueryResp> getTransferQueryRespList() {
		return transferQueryRespList;
	}

	public void setTransferQueryRespList(List<TransferQueryResp> transferQueryRespList) {
		this.transferQueryRespList = transferQueryRespList;
	}
}
