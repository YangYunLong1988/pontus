package com.snowstore.pontus.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * 项目名称：pontus-domain 类名称：MemberAttachment 类描述：会员附件表 创建人：admin 创建时间：2016年5月4日
 * 上午10:25:49 修改人：admin 修改时间：2016年5月4日 上午10:25:49 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_member_attachment")
@EntityListeners(AuditingEntityListener.class)
public class MemberAttachment extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8676280859345711975L;
	private String name;// 附件名字
	private String attachType;// 业务类型,取值件常量类中的附件业务类型
	private String objectId; // [MongoDB] ObjectID
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;// 会员
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public final static String ATTACH_TYPE_IDCARD_A = "身份证正面";
	public final static String ATTACH_TYPE_IDCARD_B = "身份证反面";
	public final static String ATTACH_TYPE_VISIT_CARD = "名片";
}
