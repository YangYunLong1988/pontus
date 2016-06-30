package com.snowstore.pontus.vo.req;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.FormParam;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.snowstore.pontus.vo.DateTimeFormat;
import com.snowstore.pontus.vo.RequestVo;

public class AddReservationReq extends RequestVo {

	@FormParam(value = "contractCode")
	@NotBlank
	@Length(max = 50)
	private String contractCode;// 合同编号

	@FormParam(value = "platform")
	@NotBlank
	@Length(max = 50)
	private String platform;// 购买平台，资产来源

	@FormParam(value = "principal")
	@NotNull
	@Digits(fraction = 2, integer = 9)
	@DecimalMin("0.01")
	private BigDecimal principal;// 投资本金

	@FormParam(value = "contractEndDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd", message = "合同到期日格式不正确")
	// @Pattern(regexp="^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$")
	@Length(min = 10, max = 10)
	private String endDate;// 合同到期日，债权到期日

	@FormParam(value = "productName")
	@Length(max = 50)
	private String productName;// 产品名称

	@FormParam(value = "term")
	@Length(max = 50)
	private String term;// 产品期限

	@FormParam(value = "yearIrr")
//	@DecimalMin("1")
//	@DecimalMax("100")
//	@Digits(fraction = 2, integer = 3)
	@Pattern(regexp="^(100|[1-9]\\d|[1-9])$",message="需填1-100整数!")
	private String yearIrr;// 年化收益率

	@FormParam(value = "paybackType")
	@Length(max = 50)
	private String paybackType;// 还款方式

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getYearIrr() {
		return yearIrr;
	}

	public void setYearIrr(String yearIrr) {
		this.yearIrr = yearIrr;
	}

}
