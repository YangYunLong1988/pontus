package com.snowstore.pontus.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.snowstore.pontus.enums.Enums;

/**
 *
 * 项目名称：pontus-domain 类名称：Transfer 类描述：出让表 创建人：admin 创建时间：2016年5月11日 下午4:47:26
 * 修改人：admin 修改时间：2016年5月11日 下午4:47:26 修改备注：
 * 
 * @version
 *
 */

@Entity
@Table(name = "pontus_transfer")
@EntityListeners(AuditingEntityListener.class)
public class Transfer extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4577387182659674478L;
	@ManyToOne
	@JoinColumn(name = "QUOTE_ID")
	private QuoteContract quoteContract;

	private BigDecimal discountRate;// 折扣率

	private String state = Enums.TransferState.VALID.getValue();// 有效，无效

	private BigDecimal transferedValue;// 挂牌后额转让价值 即 债权价值

	private BigDecimal procedureFee = BigDecimal.ZERO;// 手续费
	private BigDecimal actualPrice;// 挂牌后额折扣后转让价格

	private Date tradedTime;// 后台审核确定时间

	private String code;// 转让单编号

	private BigDecimal expProfit;// 预期收益

	private String workFlow = Enums.TransferFlow.PENDING.getValue();// 转让状态状态

	private Date endDate;// 展期到期日期，合同到期日期+10个月

	@OneToMany(mappedBy = "transfer")
	private Set<Assignee> tradedAssigneeSet = new HashSet<Assignee>();

	@Transient
	private Assignee tradedAssignee;// 预约后，后台审核交易成功时，设置其为交易成功的受让人

	public BigDecimal getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getExpProfit() {
		return expProfit;
	}

	public void setExpProfit(BigDecimal expProfit) {
		this.expProfit = expProfit;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Assignee getTradedAssignee() {
		for (Assignee assignee : tradedAssigneeSet) {
			if (Enums.AssigneeWorkFlow.AGREE.getValue().equals(assignee.getWorkFlow())) {
				return assignee;
			} else if (Enums.AssigneeWorkFlow.SUCCESS.getValue().equals(assignee.getWorkFlow())) {
				return assignee;
			}
		}
		return null;
	}

	public void setTradedAssignee(Assignee tradedAssignee) {
		this.tradedAssignee = tradedAssignee;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getTransferedValue() {
		return transferedValue;
	}

	public void setTransferedValue(BigDecimal transferedValue) {
		this.transferedValue = transferedValue;
	}

	public BigDecimal getProcedureFee() {
		return procedureFee;
	}

	public void setProcedureFee(BigDecimal procedureFee) {
		this.procedureFee = procedureFee;
	}

	public QuoteContract getQuoteContract() {
		return quoteContract;
	}

	public void setQuoteContract(QuoteContract quoteContract) {
		this.quoteContract = quoteContract;
	}

	public Date getTradedTime() {
		return tradedTime;
	}

	public void setTradedTime(Date tradedTime) {
		this.tradedTime = tradedTime;
	}

	public Set<Assignee> getTradedAssigneeSet() {
		return tradedAssigneeSet;
	}

	public void setTradedAssigneeSet(Set<Assignee> tradedAssigneeSet) {
		this.tradedAssigneeSet = tradedAssigneeSet;
	}

	/**
	 * 获取剩余时间
	 */
	@Transient
	public long getRemainingTime() {
		if (null == endDate) {
			return 0;
		}
		int days = Days.daysBetween(new DateTime().millisOfDay().withMinimumValue(), new DateTime(endDate).millisOfDay().withMinimumValue()).getDays();
		return days < 0 ? 0 : days;
	}
	@Transient
	public String getMapperWorkFlow() {
		if (Enums.TransferFlow.PENDING.getValue().equals(this.getWorkFlow())) {
			return Enums.TransferFlow.PENDING.getValue();
		} else if (Enums.TransferFlow.TRADING.getValue().equals(this.getWorkFlow()) || Enums.TransferFlow.ENSURE.getValue().equals(this.getWorkFlow())) {
			return Enums.TransferFlow.TRADING.getValue();
		} else if (Enums.TransferFlow.SUCCESS.getValue().equals(this.getWorkFlow())) {
			return Enums.TransferFlow.SUCCESS.getValue();
		} else {
			return Enums.TransferFlow.CANCEL.getValue();
		}
	}
}
