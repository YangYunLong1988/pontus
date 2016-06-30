package com.snowstore.pontus.vo.resp;


public class TransferQueryResp {

	private String transferId; // 转让id

	private String quoteContractId;// 挂牌合同id

	private String contractCode;// 合同编号

	private String principal;// 投资本金

	private String transferedValue;// 债权价值

	private String expProfit;// 预期收益

	private String actualPrice;// 转让价格

	private String discountRate;// 折扣率

	private String endDate;// 展期到期日期，合同到期日期+10个月

	private String workFlow;// 转让状态状态

	private int bespeakCount;// 预约人数
	
	/** 剩余期限	必填项	取值展期后的剩余期限=合同到期日+10个月-当前日期（当前日期不算）**/
	private int leftDays;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getTransferedValue() {
		return transferedValue;
	}

	public void setTransferedValue(String transferedValue) {
		this.transferedValue = transferedValue;
	}

	public String getExpProfit() {
		return expProfit;
	}

	public void setExpProfit(String expProfit) {
		this.expProfit = expProfit;
	}

	public String getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(String actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public int getBespeakCount() {
		return bespeakCount;
	}

	public void setBespeakCount(int bespeakCount) {
		this.bespeakCount = bespeakCount;
	}

	public String getQuoteContractId() {
		return quoteContractId;
	}

	public void setQuoteContractId(String quoteContractId) {
		this.quoteContractId = quoteContractId;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public int getLeftDays() {
		return leftDays;
	}

	public void setLeftDays(int leftDays) {
		this.leftDays = leftDays;
	}
}
