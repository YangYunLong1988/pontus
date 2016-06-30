package com.snowstore.pontus.vo.resp;

import java.util.List;

import com.snowstore.pontus.domain.BranchBank;
import com.snowstore.pontus.vo.ResponseVo;

public class BankSubbranchResp extends ResponseVo {
	List<BranchBank> branchBankList;

	public List<BranchBank> getBranchBankList() {
		return branchBankList;
	}

	public void setBranchBankList(List<BranchBank> branchBankList) {
		this.branchBankList = branchBankList;
	}
}
