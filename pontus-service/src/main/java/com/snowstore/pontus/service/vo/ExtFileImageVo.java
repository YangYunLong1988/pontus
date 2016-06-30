package com.snowstore.pontus.service.vo;

import java.io.Serializable;

public class ExtFileImageVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3281367230519934489L;

	private Integer totalNo;// 总页数
	private Integer curNo;// 当前页数
	private String content;// 图片base64内容

	public Integer getTotalNo() {
		return totalNo;
	}

	public void setTotalNo(Integer totalNo) {
		this.totalNo = totalNo;
	}

	public Integer getCurNo() {
		return curNo;
	}

	public void setCurNo(Integer curNo) {
		this.curNo = curNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
