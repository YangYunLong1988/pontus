package com.snowstore.pontus.service.vo;

public class IdentityForm {
	private String idCardName;
	private String idCardAccount;
	private String bankName;
	private String bankAccount;
	private String bankProvince;
	private String bankCity;
	private String bankSubbranch;
	private String residenceProvince;
	private String residenceCity;

	private Long idCardImageA;
	private Long idCardImageB;
	private Long visitCardImage;

	public String getIdCardName() {
		return idCardName;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public String getIdCardAccount() {
		return idCardAccount;
	}

	public void setIdCardAccount(String idCardAccount) {
		this.idCardAccount = idCardAccount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBankSubbranch() {
		return bankSubbranch;
	}

	public void setBankSubbranch(String bankSubbranch) {
		this.bankSubbranch = bankSubbranch;
	}

	public String getResidenceProvince() {
		return residenceProvince;
	}

	public void setResidenceProvince(String residenceProvince) {
		this.residenceProvince = residenceProvince;
	}

	public String getResidenceCity() {
		return residenceCity;
	}

	public void setResidenceCity(String residenceCity) {
		this.residenceCity = residenceCity;
	}

	public Long getIdCardImageA() {
		return idCardImageA;
	}

	public void setIdCardImageA(Long idCardImageA) {
		this.idCardImageA = idCardImageA;
	}

	public Long getIdCardImageB() {
		return idCardImageB;
	}

	public void setIdCardImageB(Long idCardImageB) {
		this.idCardImageB = idCardImageB;
	}

	public Long getVisitCardImage() {
		return visitCardImage;
	}

	public void setVisitCardImage(Long visitCardImage) {
		this.visitCardImage = visitCardImage;
	}

}
