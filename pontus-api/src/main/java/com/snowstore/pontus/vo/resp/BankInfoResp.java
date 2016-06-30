package com.snowstore.pontus.vo.resp;

public class BankInfoResp {

	private Long id;
	private String name;// 银行名称
	private String tailAccount;// 账号
	private String defaulted;// 是否默认银行卡

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTailAccount() {
		return tailAccount;
	}

	public void setTailAccount(String tailAccount) {
		this.tailAccount = tailAccount;
	}

	public void setDefaulted(String defaulted) {
		this.defaulted = defaulted;
	}

	public String getDefaulted() {
		return defaulted;
	}

}
