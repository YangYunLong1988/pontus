package com.snowstore.pontus.vo.resp;

import java.util.ArrayList;
import java.util.List;

import com.snowstore.pontus.vo.ResponseVo;


public class SpecialContractListResp extends ResponseVo {

	private List<SpecialContractResp> specialContractList = new ArrayList<SpecialContractResp>();

	public List<SpecialContractResp> getSpecialContractList() {
		return specialContractList;
	}

	public void setSpecialContractList(List<SpecialContractResp> specialContractList) {
		this.specialContractList = specialContractList;
	}

}
