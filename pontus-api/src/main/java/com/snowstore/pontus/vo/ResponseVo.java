package com.snowstore.pontus.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(Include.NON_NULL)
public class ResponseVo {

	private RespCode status = RespCode.OK;

	private String memo;

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public RespCode getStatus() {
		return status;
	}

	public void setStatus(RespCode status) {
		this.status = status;
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum RespCode {
		OK("000000"), ERROR("999999"), DENIED("888888");

		private String value;

		private RespCode(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

	}
}
