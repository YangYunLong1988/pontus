package com.snowstore.pontus.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * 项目名称：pontus-domain 类名称：SpecialDetail 类描述：特殊兑付明细 创建人：admin 创建时间：2016年6月16日
 * 下午4:47:04 修改人：admin 修改时间：2016年6月16日 下午4:47:04 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_special_detail")
@EntityListeners(AuditingEntityListener.class)
public class SpecialDetail extends AbstractEntity {
	// Fields
	private static final long serialVersionUID = 1919772319603609131L;
	private BigDecimal dealAmount;// 兑付金额
	private Date dealDate;// 兑付日期
	private String dealReason;// 兑付原因

	// Cascade
	@ManyToOne
	@JoinColumn(name = "special_id")
	private SpecialContract specialContract;

	// Constructors
	/*** default constructor ***/
	public SpecialDetail() {

	}

	// Property accessor
	public BigDecimal getDealAmount() {
		return dealAmount;
	}

	public void setDealAmount(BigDecimal dealAmount) {
		this.dealAmount = dealAmount;
	}

	public Date getDealDate() {
		return dealDate;
	}

	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}

	public String getDealReason() {
		return dealReason;
	}

	public void setDealReason(String dealReason) {
		this.dealReason = dealReason;
	}

	public SpecialContract getSpecialContract() {
		return specialContract;
	}

	public void setSpecialContract(SpecialContract specialContract) {
		this.specialContract = specialContract;
	}
}
