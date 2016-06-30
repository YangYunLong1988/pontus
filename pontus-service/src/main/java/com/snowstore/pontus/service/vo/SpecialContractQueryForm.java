package com.snowstore.pontus.service.vo;

public class SpecialContractQueryForm {
	private String platform;// 产品来源
	private String query;// 姓名，手机号，合同编号

	public String getPlatform() {
		if ("全部".equals(platform)) {
			return null;
		}
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
