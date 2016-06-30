package com.snowstore.pontus.vo.req;

import javax.ws.rs.FormParam;
import org.hibernate.validator.constraints.NotBlank;
import com.snowstore.pontus.vo.RequestVo;

public class TransferRecordListReq extends RequestVo {

	@FormParam(value = "page")
	@NotBlank
	private String page;

	@FormParam(value = "rows")
	@NotBlank
	private String rows;

	@FormParam(value = "orderBy")
	private String orderBy;

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
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

	@Override
	public String toString() {
		return "TransferRecordListReq [page=" + page + ", rows=" + rows + ", orderBy=" + orderBy + ", getAccessToken()=" + getAccessToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
