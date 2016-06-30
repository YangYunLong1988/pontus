package com.snowstore.pontus.vo.req;


import javax.ws.rs.FormParam;

import com.snowstore.pontus.vo.RequestVo;

public class BankListReq extends RequestVo {

	@FormParam(value = "page")
	private int currentPage;
	@FormParam(value = "rows")
	private int pageSize;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


}
