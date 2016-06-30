package com.snowstore.pontus.vo.req;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.vo.RequestVo;

public class FileReq extends RequestVo {

	/**
	 * 允许为空
	 */
	@FormDataParam(value = "subjectId")
	private String subjectId;

	@FormDataParam(value = "subjectType")
	@NotBlank
	private String subjectType;
	
	/**
	 * 允许为空
	 */
	@FormDataParam(value = "attachTableAlias")
	private String attachTableAlias;

	@FormDataParam(value = "file")
	private InputStream file;

	@FormDataParam("file")
	FormDataContentDisposition fileDisposition;

	public InputStream getFile() {
		return file;
	}

	public void setFile(InputStream file) {
		this.file = file;
	}

	public FormDataContentDisposition getFileDisposition() {
		return fileDisposition;
	}

	public void setFileDisposition(FormDataContentDisposition fileDisposition) {
		this.fileDisposition = fileDisposition;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getAttachTableAlias() {
		return attachTableAlias;
	}

	public void setAttachTableAlias(String attachTableAlias) {
		this.attachTableAlias = attachTableAlias;
	}

	@Override
	public String toString() {
		return "FileReq [subjectId=" + subjectId + ", subjectType=" + subjectType + ", attachTableAlias=" + attachTableAlias + ", file=" + file + ", fileDisposition=" + fileDisposition + ", getAccessToken()=" + getAccessToken() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
