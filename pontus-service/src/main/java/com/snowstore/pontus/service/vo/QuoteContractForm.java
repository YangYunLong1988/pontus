package com.snowstore.pontus.service.vo;

import java.util.List;

import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;

public class QuoteContractForm extends PageFormVo {

	private Long customerId;

	private List<QuoteContractWorkFlow> workFlowList;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public List<QuoteContractWorkFlow> getWorkFlowList() {
		return workFlowList;
	}

	public void setWorkFlowList(List<QuoteContractWorkFlow> workFlowList) {
		this.workFlowList = workFlowList;
	}

}
