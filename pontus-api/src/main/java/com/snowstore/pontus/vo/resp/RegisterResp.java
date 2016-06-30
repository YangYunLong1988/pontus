package com.snowstore.pontus.vo.resp;

import com.snowstore.pontus.vo.ResponseVo;

public class RegisterResp extends ResponseVo{

	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
