package com.snowstore.pontus.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * 项目名称：pontus-domain 类名称：AssigneeAttachment 类描述：预约交易成功的客户上传的线下交易附件 创建人：admin
 * 创建时间：2016年5月11日 下午5:02:49 修改人：admin 修改时间：2016年5月11日 下午5:02:49 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_ass_attach")
@EntityListeners(AuditingEntityListener.class)
public class AssigneeAttachment extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1695692061511892987L;
	@ManyToOne
	@JoinColumn(name = "ASSIGNEE_ID")
	private Assignee assignee;
	private String name;// 附件名字
	private String attachType;// 业务类型,取值件常量类中的附件业务类型
	private String objectId; // [MongoDB] ObjectID
	private String state;// 状态

	public Assignee getAssignee() {
		return assignee;
	}

	public void setAssignee(Assignee assignee) {
		this.assignee = assignee;
	}

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
