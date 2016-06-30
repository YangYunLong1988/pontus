package com.snowstore.pontus.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * 项目名称：pontus-domain 类名称：OriginalContractAttachment 类描述：原始合同附件表 创建人：admin
 * 创建时间：2016年5月4日 下午12:26:27 修改人：admin 修改时间：2016年5月4日 下午12:26:27 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_original_attach")
@EntityListeners(AuditingEntityListener.class)
public class OriginalContractAttachment extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8722534621855000321L;
	private String name;// 附件名字
	private String attachType;// 业务类型,取值件常量类中的附件业务类型
	private String objectId; // [MongoDB] ObjectID
	@ManyToOne
	@JoinColumn(name = "CONTRACT_ID")
	private OriginalContract originalContract;// 原始合同
	private String status;

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

	
	public OriginalContract getOriginalContract() {
		return originalContract;
	}

	public void setOriginalContract(OriginalContract originalContract) {
		this.originalContract = originalContract;
	}

}
