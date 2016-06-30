package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;

import com.snowstore.pontus.vo.RequestVo;

public class FileSignReq extends RequestVo {
	@FormParam(value = "subjectId")
	private Long subjectId;// id

	@FormParam(value = "subjectType")
	private String subjectType;// 类型

	@FormParam(value = "validateCode")
	private String validateCode;

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	@Override
	public String toString() {
		return "FileSignReq [subjectId=" + subjectId + ", subjectType=" + subjectType + ", validateCode=" + validateCode + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
