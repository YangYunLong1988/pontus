package com.snowstore.pontus.vo.resp;

import java.util.List;

import com.snowstore.pontus.vo.ResponseVo;

public class AssigneeRecordListResp extends ResponseVo {

	private List<AssigneeRecordResp> assigneeRecordRespList;

	public List<AssigneeRecordResp> getAssigneeRecordRespList() {
		return assigneeRecordRespList;
	}

	public void setAssigneeRecordRespList(List<AssigneeRecordResp> assigneeRecordRespList) {
		this.assigneeRecordRespList = assigneeRecordRespList;
	}

}
