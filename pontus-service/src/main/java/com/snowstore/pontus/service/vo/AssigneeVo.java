package com.snowstore.pontus.service.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.snowstore.pontus.enums.Enums;

public class AssigneeVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String code;// 受让单编号，转让单编号+001,002….序列生成
	private String workFlow;// 受让状态
	private String attachFlow = Enums.AssigneeWorkAttachFlow.NONE.getValue();// 附件状态
	private BigDecimal assigneePrice = BigDecimal.ZERO;// 预约当天的价格
	private Date createdDate;// 受让时间
	private Long agreeAssigneeId = null;// 同意预约id

	// 用户Customer
	private String idCardName;// 受让人姓名
	private String phone;// 受让人电话

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public String getAttachFlow() {
		return attachFlow;
	}

	public void setAttachFlow(String attachFlow) {
		this.attachFlow = attachFlow;
	}

	public BigDecimal getAssigneePrice() {
		return assigneePrice;
	}

	public void setAssigneePrice(BigDecimal assigneePrice) {
		this.assigneePrice = assigneePrice;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getAgreeAssigneeId() {
		return agreeAssigneeId;
	}

	public void setAgreeAssigneeId(Long agreeAssigneeId) {
		this.agreeAssigneeId = agreeAssigneeId;
	}

	public String getIdCardName() {
		return idCardName;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
