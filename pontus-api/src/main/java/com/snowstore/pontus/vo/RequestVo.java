package com.snowstore.pontus.vo;


import javax.ws.rs.QueryParam;

public class RequestVo {

	@QueryParam("accessToken")
	// @FormParam("accessToken")
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
