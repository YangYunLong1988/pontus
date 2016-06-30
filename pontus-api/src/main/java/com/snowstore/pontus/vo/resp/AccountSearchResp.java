package com.snowstore.pontus.vo.resp;

import com.snowstore.pontus.vo.ResponseVo;

public class AccountSearchResp extends ResponseVo {

	private String idCardName;
	private String idCardAccount;
	private int bankCount;

	public String getIdCardAccount() {
		return idCardAccount;
	}

	public void setIdCardAccount(String idCardAccount) {
		this.idCardAccount = idCardAccount;
	}


	public String getIdCardName() {
		return idCardName;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public int getBankCount() {
		return bankCount;
	}

	public void setBankCount(int bankCount) {
		this.bankCount = bankCount;
	}

}
