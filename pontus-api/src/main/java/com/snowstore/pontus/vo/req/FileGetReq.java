package com.snowstore.pontus.vo.req;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.vo.RequestVo;

public class FileGetReq extends RequestVo {
	@FormParam(value = "subjectId")
	@NotNull
	private Long subjectId;// id

	@FormParam(value = "subjectType")
	@NotBlank
	private String subjectType;// 类型

	@FormParam(value = "currentPage")
	@Min(1)
	private int currentPage;

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public String toString() {
		return "FileGetReq [subjectId=" + subjectId + ", subjectType=" + subjectType + ", currentPage=" + currentPage + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
