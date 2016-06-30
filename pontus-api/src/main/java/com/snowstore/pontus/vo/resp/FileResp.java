package com.snowstore.pontus.vo.resp;

import com.snowstore.pontus.vo.ResponseVo;

public class FileResp extends ResponseVo {

	private Long memberAttachId;

	public Long getMemberAttachId() {
		return memberAttachId;
	}

	public void setMemberAttachId(Long memberAttachId) {
		this.memberAttachId = memberAttachId;
	}
}
