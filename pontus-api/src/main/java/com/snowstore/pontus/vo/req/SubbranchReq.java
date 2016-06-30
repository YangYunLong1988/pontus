package com.snowstore.pontus.vo.req;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.snowstore.pontus.vo.RequestVo;

public class SubbranchReq extends RequestVo {

	@FormDataParam(value = "file")
	private InputStream file;

	@FormDataParam("file")
	FormDataContentDisposition fileDisposition;

	@FormDataParam(value = "fileType")
	private String fileType;

	public InputStream getFile() {
		return file;
	}

	public void setFile(InputStream file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public FormDataContentDisposition getFileDisposition() {
		return fileDisposition;
	}

	public void setFileDisposition(FormDataContentDisposition fileDisposition) {
		this.fileDisposition = fileDisposition;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}
