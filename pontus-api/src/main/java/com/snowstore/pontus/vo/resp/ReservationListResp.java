package com.snowstore.pontus.vo.resp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.snowstore.pontus.service.vo.BigDecimalSerializer;
import com.snowstore.pontus.vo.ResponseVo;

public class ReservationListResp extends ResponseVo {

	private List<ReservationResp> reservationList = new ArrayList<ReservationListResp.ReservationResp>();

	public List<ReservationResp> getReservationList() {
		return reservationList;
	}

	public void setReservationList(List<ReservationResp> reservationList) {
		this.reservationList = reservationList;
	}

	public static class ReservationResp {
		private String contractCode;// 合同编号
		private String platform;// 购买平台，资产来源
		private BigDecimal principal;// 投资本金
		private BigDecimal yearIrr;// 收益
		private String workFlow;
		private String productName;// 产品名称
		private String endDate;// 合同到期日，债权到期日
		private String term;// 产品期限
		private String paybackType;// 还款方式
		private Long reservationId;// 挂牌编号

		public Long getReservationId() {
			return reservationId;
		}

		public void setReservationId(Long reservationId) {
			this.reservationId = reservationId;
		}

		public String getContractCode() {
			return contractCode;
		}

		public void setContractCode(String contractCode) {
			this.contractCode = contractCode;
		}

		public BigDecimal getYearIrr() {
			return yearIrr;
		}

		public void setYearIrr(BigDecimal yearIrr) {
			this.yearIrr = yearIrr;
		}

		public String getWorkFlow() {
			return workFlow;
		}

		public void setWorkFlow(String workFlow) {
			this.workFlow = workFlow;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}

		@JsonSerialize(using = BigDecimalSerializer.class)
		public BigDecimal getPrincipal() {
			return principal;
		}

		public void setPrincipal(BigDecimal principal) {
			this.principal = principal;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getTerm() {
			return term;
		}

		public void setTerm(String term) {
			this.term = term;
		}

		public String getPaybackType() {
			return paybackType;
		}

		public void setPaybackType(String paybackType) {
			this.paybackType = paybackType;
		}

	}

}
