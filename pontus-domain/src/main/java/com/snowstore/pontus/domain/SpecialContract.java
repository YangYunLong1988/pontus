package com.snowstore.pontus.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * 项目名称：pontus-domain 类名称：SpecialContract 类描述：特殊对付 创建人：admin 创建时间：2016年6月16日 下午4:47:04 修改人：admin 修改时间：2016年6月16日 下午4:47:04 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_special_contract")
@EntityListeners(AuditingEntityListener.class)
public class SpecialContract extends AbstractEntity {
	// Fields
	private static final long serialVersionUID = -3975343829570542754L;
	// Cascade
	@OneToOne
	@JoinColumn(name = "contract_id")
	private OriginalContract originalContract;// 原始合同

	@JsonIgnore
	@OneToMany(mappedBy = "specialContract", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<SpecialDetail> specialDetailSet = new HashSet<SpecialDetail>();

	public OriginalContract getOriginalContract() {
		return originalContract;
	}

	public void setOriginalContract(OriginalContract originalContract) {
		this.originalContract = originalContract;
	}

	public Set<SpecialDetail> getSpecialDetailSet() {
		return specialDetailSet;
	}

	public void setSpecialDetailSet(Set<SpecialDetail> specialDetailSet) {
		this.specialDetailSet = specialDetailSet;
	}

	@Transient
	public BigDecimal getBuyBackAmount() {
		BigDecimal buyBackAmount = BigDecimal.ZERO;
		for (SpecialDetail specialDetail : specialDetailSet) {
			buyBackAmount = buyBackAmount.add(specialDetail.getDealAmount());
		}
		return buyBackAmount;
	}
}
