package com.snowstore.pontus.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * 项目名称：pontus-domain 类名称：QuoteContractAttachment 类描述：挂牌合同附件 创建人：admin
 * 创建时间：2016年5月4日 下午12:36:21 修改人：admin 修改时间：2016年5月4日 下午12:36:21 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_quote_attachment")
@EntityListeners(AuditingEntityListener.class)
public class QuoteContractAttachment extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5218813636595418047L;

	private String name;// 附件名字
	private String attachType;// 业务类型,取值件常量类中的附件业务类型
	private String objectId; // [MongoDB] ObjectID
	@ManyToOne
	@JoinColumn(name = "CONTRACT_ID")
	private QuoteContract quoteContract;// 挂牌合同
	private String status;// 状态

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttachType() {
		return attachType;
	}

	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public QuoteContract getQuoteContract() {
		return quoteContract;
	}

	public void setQuoteContract(QuoteContract quoteContract) {
		this.quoteContract = quoteContract;
	}

}
