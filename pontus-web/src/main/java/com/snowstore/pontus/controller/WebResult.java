package com.snowstore.pontus.controller;

import java.io.Serializable;

public class WebResult implements Serializable {
	private static final long serialVersionUID = -8852752251470467521L;
	private int code;
	private Object content;

	public WebResult(int code, Object content) {
		super();
		this.code = code;
		this.content = content;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public final static int CODE_SUCCESS = 0;
	public final static int CODE_EXCEPTION = 1;

	public static WebResult newExceptionWebResult() {
		return newExceptionWebResult("");
	}

	public static WebResult newExceptionWebResult(String content) {
		return new WebResult(CODE_EXCEPTION, content);
	}

	public static WebResult newSuccessWebResult() {
		return newSuccessWebResult(null);
	}

	public static WebResult newSuccessWebResult(Object content) {
		return new WebResult(CODE_SUCCESS, content);
	}
}
