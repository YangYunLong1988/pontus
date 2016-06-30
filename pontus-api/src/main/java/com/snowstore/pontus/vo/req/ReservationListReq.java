package com.snowstore.pontus.vo.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.FormParam;

import com.snowstore.pontus.vo.RequestVo;

public class ReservationListReq extends RequestVo {

	@FormParam(value = "page")
	@Max(99)
	@Min(0)
	private Long page = 1l;

	@FormParam(value = "rows")
	@Max(15)
	@Min(0)
	private Long rows = 10l;

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public Long getRows() {
		return rows;
	}

	public void setRows(Long rows) {
		this.rows = rows;
	}

}
