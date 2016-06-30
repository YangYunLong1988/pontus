package com.snowstore.pontus.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "pontus_ext_file_img")
@EntityListeners(AuditingEntityListener.class)
public class ExtendFileImage extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 548072229621006166L;
	private Integer pageNo;// 第几页
	private String objectId; // mongodb 的图片id
	@ManyToOne()
	@JoinColumn(name = "FILE_ID")
	private RenewalAgreement pdfFile;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public RenewalAgreement getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(RenewalAgreement pdfFile) {
		this.pdfFile = pdfFile;
	}

}
