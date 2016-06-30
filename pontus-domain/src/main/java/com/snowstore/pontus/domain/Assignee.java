package com.snowstore.pontus.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.AssigneeWorkFlow;

/**
 *
 * 项目名称：pontus-domain 类名称：Assignee 类描述：受让预约表 创建人：admin 创建时间：2016年5月11日 下午4:47:04
 * 修改人：admin 修改时间：2016年5月11日 下午4:47:04 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_assignee")
@EntityListeners(AuditingEntityListener.class)
public class Assignee extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8124085290870051114L;
	@ManyToOne
	@JoinColumn(name = "TRANSFER_ID")
	@JsonIgnore
	private Transfer transfer;

	private String state;// 对应操作状态

	private String workFlow;// 受让状态

	private String code;// 受让单编号，转让单编号+001,002….序列生成

	private String attachFlow = Enums.AssigneeWorkAttachFlow.NONE.getValue();// 附件状态

	private BigDecimal assigneePrice = BigDecimal.ZERO;// 预约当天的价格
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;// 受让人，投资者
	@OneToOne(optional=false)
	@JoinColumn(name = "BANK_ID")
	private BankInfo bankInfo;

	public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAttachFlow() {
		return attachFlow;
	}

	public void setAttachFlow(String attachFlow) {
		this.attachFlow = attachFlow;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getAssigneePrice() {
		return assigneePrice;
	}

	public void setAssigneePrice(BigDecimal assigneePrice) {
		this.assigneePrice = assigneePrice;
	}

	public BankInfo getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(BankInfo bankInfo) {
		this.bankInfo = bankInfo;
	}

	@Transient
	public String getMapperWorkFlow() {
		if (AssigneeWorkFlow.REJECT.getValue().equals(this.getWorkFlow())) {
			return "已取消";
		} else if (AssigneeWorkFlow.APPOINT.getValue().equals(this.getWorkFlow()) || AssigneeWorkFlow.AGREE.getValue().equals(this.getWorkFlow())) {
			return "交易中";
		} else if (AssigneeWorkFlow.SUCCESS.getValue().equals(this.getWorkFlow())) {
			return "交易成功";
		} else {
			return "";
		}
	}
}
