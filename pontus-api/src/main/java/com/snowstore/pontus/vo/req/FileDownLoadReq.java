package com.snowstore.pontus.vo.req;


import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.vo.RequestVo;

public class FileDownLoadReq extends RequestVo {

	@FormParam(value = "subjectId")
	private Long subjectId;

	@FormParam(value = "subjectType")
	@NotBlank
	private String subjectType;
	
	@FormParam(value = "attachTableAlias")
	private String attachTableAlias;

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getAttachTableAlias() {
		return attachTableAlias;
	}

	public void setAttachTableAlias(String attachTableAlias) {
		this.attachTableAlias = attachTableAlias;
	}

	@Override
	public String toString() {
		return "FileDownLoadReq [subjectId=" + subjectId + ", subjectType=" + subjectType + ", attachTableAlias=" + attachTableAlias + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
