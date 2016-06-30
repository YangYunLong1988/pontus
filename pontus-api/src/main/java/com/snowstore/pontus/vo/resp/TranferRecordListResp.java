package com.snowstore.pontus.vo.resp;

import java.util.List;

import com.snowstore.pontus.vo.ResponseVo;

public class TranferRecordListResp extends ResponseVo {

	private List<TranferRecordResp> tranferRecordRespList;

	public List<TranferRecordResp> getTranferRecordRespList() {
		return tranferRecordRespList;
	}

	public void setTranferRecordRespList(List<TranferRecordResp> tranferRecordRespList) {
		this.tranferRecordRespList = tranferRecordRespList;
	}

}
