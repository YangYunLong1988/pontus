package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;
import org.hibernate.validator.constraints.NotBlank;
import com.snowstore.pontus.vo.RequestVo;

public class SpecialContractReq extends RequestVo {

	
	@FormParam(value = "page")
	@NotBlank
	private String page;

	@FormParam(value = "rows")
	@NotBlank
	private String rows;

	public String toString() {
		return "SpecialContractReq [page=" + page + ", rows=" + rows + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

}
