package com.snowstore.pontus.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * 项目名称：pontus-domain 类名称：RenewalAgreement 类描述：展期协议 创建人：admin 创建时间：2016年5月4日
 * 下午12:41:17 修改人：admin 修改时间：2016年5月4日 下午12:41:17 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_renewal_agreement")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties("quoteContract")
public class RenewalAgreement extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3984472703392971339L;
	@OneToOne
	@JoinColumn(name = "CONTRACT_ID")
	private QuoteContract quoteContract;// 所属合同
	private String workFlow;// 工作流
	private String fileName;// 协议名称
	private String objectId;// 协议ID
	private String signDate;// 签署时间
	private Integer totalPages;// 协议总页数

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public QuoteContract getQuoteContract() {
		return quoteContract;
	}

	public void setQuoteContract(QuoteContract quoteContract) {
		this.quoteContract = quoteContract;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

}
