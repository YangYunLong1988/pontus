package com.snowstore.pontus.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

	private final int PRECISION = 10;

	/**
	 * 乘法运算
	 * 
	 * @param v1
	 * 
	 * 
	 * @param v2
	 * 
	 * 
	 * @return
	 */

	public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
		v1 = v1 == null ? new BigDecimal(0) : v1;
		v2 = v2 == null ? new BigDecimal(0) : v2;

		BigDecimal b1 = new BigDecimal(v1.toString());

		BigDecimal b2 = new BigDecimal(v2.toString());

		return b1.multiply(b2);

	}

	/**
	 * 展期年华收益率，目前固定为6%
	 * 
	 * @return
	 */
	public BigDecimal extRate() {
		return new BigDecimal(6);
	}

	/**
	 * 获取展期后到期期限，暂时为合同期限+10个月
	 * 
	 * @param date
	 * @return
	 */
	public Date getExtDate(Date contractEndDate) {
		return new DateTime(contractEndDate).plusMonths(10).toDate();
	}

	/**
	 * 展期收益 待回购金额*展期年化收益率*（6+4）/12 待回购金额 = 投资本金+未还的利息
	 * 
	 * @param tobackAmount
	 * @return
	 */
	public BigDecimal extProfit(BigDecimal tobackAmount) {
		return mul(tobackAmount, extRate()).multiply(new BigDecimal(10)).divide(new BigDecimal(1200), 2, BigDecimal.ROUND_FLOOR);
	}

	/**
	 * 债权价值 系统自动计算已审核通过的挂牌合同展期后的债权价值 =（投资本金+未还的利息）*（1+展期年化收益率*10/12）
	 * 
	 * @param tobackAmount
	 * @return
	 */
	public BigDecimal extValue(BigDecimal tobackAmount) {
		return extProfit(tobackAmount).add(tobackAmount);
	}

	/**
	 * 预期收益 =债权价值
	 * 
	 * @param tobackAmount
	 * @return
	 */
	public BigDecimal expectProfit(BigDecimal tobackAmount) {
		return extValue(tobackAmount);
	}

	/**
	 * 当前日期小于合同到期日时候的转让价格计算 1） 当期日期 < 合同到期日期； 1.1） 还款方式为“到期还本付息”
	 * =折扣率*投资本金*（1+年化收益率*（当前日期-起息日）/365）
	 * 
	 * @param contractEndDate
	 * @param repayMode
	 * @param investPrincipal
	 * @param discount 折价率
	 * @return
	 */
	public BigDecimal quotePriceWhenLtContractEndDateByOnePay(Date interestStartDate, BigDecimal investPrincipal, BigDecimal irrRate, BigDecimal discount) {
		BigDecimal rate = irrRate.multiply(new BigDecimal(getDaysBetweenDate(interestStartDate, new Date()))).divide(new BigDecimal(36500), PRECISION, BigDecimal.ROUND_FLOOR).add(new BigDecimal(1));
		return discount.multiply(investPrincipal).multiply(rate).divide(new BigDecimal(100), 2, BigDecimal.ROUND_FLOOR);

	}

	/**
	 * 当前日期小于合同到期日时候的转让价格计算 1） 当期日期 < 合同到期日期； 1.1） 还款方式为“月/季/半付息，到期还本
	 * =折扣率*（本金+【未还的利息-（合同到期日期-当前日期）/（合同到期日期-起息日）*（本息和-投资本金）】）
	 * 
	 * @param contractEndDate 合同到期日
	 * @param interestStartDate 起息日
	 * @param investPrincipal 投资本金
	 * @param unpayedInterest 未还利息
	 * @param totalAmount 本息和
	 * @param discount 折扣率
	 * @return
	 */
	public BigDecimal quotePriceWhenLtContractEndDateByManyPay(Date contractEndDate, Date interestStartDate, BigDecimal investPrincipal, BigDecimal unpayedInterest, BigDecimal totalAmount, BigDecimal discount) {
		return discount.multiply(
				investPrincipal.add(unpayedInterest.subtract(new BigDecimal(getDaysBetweenDate(new Date(), contractEndDate)).multiply(totalAmount.subtract(investPrincipal)).divide(new BigDecimal(getDaysBetweenDate(interestStartDate, contractEndDate)), PRECISION,
						BigDecimal.ROUND_FLOOR)))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_FLOOR);
	}

	/**
	 * 2） 当前日期 = 合同到期日； =折价率*（投资本金+未还的利息）
	 * 
	 * @param investPrincipal
	 * @param unpayedInterest
	 * @return
	 */
	public BigDecimal quotePriceWhenEqContractEndDate(BigDecimal investPrincipal, BigDecimal unpayedInterest, BigDecimal discount) {
		return discount.multiply(investPrincipal.add(unpayedInterest)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_FLOOR);
	}

	/**
	 * 3） 当前日期 > 合同到期日 =折扣率* （投资本金+未还的利息）*（1+展期年化收益率*（当前日期-合同到期日期）/365）
	 * 
	 * @param totalAmount 投资本金+未还利息
	 * @param discount 折扣率
	 * @param contractEndDate
	 * @return
	 */
	public BigDecimal quotePriceWhenGtContractEndDate(BigDecimal totalAmount, BigDecimal discount, Date contractEndDate) {
		BigDecimal rate = extRate().multiply(new BigDecimal(getDaysBetweenDate(contractEndDate, new Date()))).divide(new BigDecimal(36500), PRECISION, BigDecimal.ROUND_FLOOR).add(new BigDecimal(1));
		return discount.multiply(totalAmount).multiply(rate).divide(new BigDecimal(100), 2, BigDecimal.ROUND_FLOOR);
	}

	/**
	 * 取值展期后的剩余期限 =合同到期日+10个月-当前日期（当前日期不算）
	 * 
	 * @param contractEndDate
	 * @return
	 */
	public Integer leftTerm(Date contractEndDate) {
		return getDaysBetweenDate(new Date(), getExtDate(contractEndDate));
	}

	/**
	 * 取值展期后的剩余期限 =合同到期日+10个月-当前日期（当前日期不算）
	 * 
	 * @param transferEndDate 转让表 到期日期（已是合同到期日+10个月）
	 * @return
	 */
	public Integer leftTermForTransfer(Date transferEndDate) {
		return getDaysBetweenDate(new Date(), transferEndDate);
	}

	/**
	 * 返回手续费，当前固定为0
	 * 
	 * @return
	 */
	public BigDecimal getProcedureFee() {
		return BigDecimal.ZERO;
	}

	/**
	 * 
	 * @description: 获取当天开始时间(24小时制)
	 * @author: gab
	 * @return
	 */
	public static Date getInitTime(Date date) {
		Calendar start = Calendar.getInstance();
		start.setTime(date);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		return start.getTime();
	}

	public static int getDaysBetweenDate(Date date1, Date date2) {
		long time1 = getInitTime(date1).getTime();
		long time2 = getInitTime(date2).getTime();
		long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(betweenDays));
	}
}
