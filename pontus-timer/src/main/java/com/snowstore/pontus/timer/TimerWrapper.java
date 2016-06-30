package com.snowstore.pontus.timer;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.snowstore.pontus.domain.Transfer;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.service.TransferService;
import com.snowstore.pontus.service.vo.SimpleTransferQueryForm;

@Service
public class TimerWrapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimerWrapper.class);
	@Autowired
	private TransferService transferService;

	@Scheduled(cron = "0 0 0 * * ?")
	public void updateTransferActualPrice() {
		SimpleTransferQueryForm form = new SimpleTransferQueryForm();
		form.setWorkFlow(Arrays.asList(Enums.TransferFlow.PENDING.getValue(), Enums.TransferFlow.TRADING.getValue()));
		List<Transfer> transfers = transferService.queryTransfer(form);
		LOGGER.info("更新转让价格 - {}", transfers.size());
		for (Transfer transfer : transfers) {
			transfer.setActualPrice(transferService.getTransferPrice(transfer.getQuoteContract(), transfer.getDiscountRate()));
			transferService.save(transfer);
		}
	}
	/*
	 * @SuppressWarnings("unused") private BigDecimal calculation(Transfer
	 * transfer) { BigDecimal actualPrice = null; DateTime now = new
	 * DateTime().millisOfDay().withMinimumValue(); DateTime endDate = new
	 * DateTime(transfer.getQuoteContract().getContractEndDate()).millisOfDay().
	 * withMinimumValue(); DateTime interestStartDate = new
	 * DateTime(transfer.getQuoteContract().getInterestStartDate()).millisOfDay(
	 * ).withMinimumValue(); BigDecimal discountRate =
	 * transfer.getDiscountRate().divide(new BigDecimal(100)); if
	 * (endDate.isAfter(now)) { DateTime beginTime = null; if
	 * (Enums.RepaymentMode.END.getValue().equals(transfer.getQuoteContract().
	 * getPaybackType())) { beginTime = new
	 * DateTime(transfer.getQuoteContract().getInterestStartDate()).millisOfDay(
	 * ).withMinimumValue(); int days = new Period(beginTime, now).getDays();
	 * BigDecimal tmp =
	 * BigDecimal.ONE.add(transfer.getQuoteContract().getYearIrr().divide(new
	 * BigDecimal(100))).multiply(new BigDecimal(days)).divide(new
	 * BigDecimal(365), 2, RoundingMode.FLOOR); actualPrice =
	 * discountRate.multiply(transfer.getQuoteContract().getPrincipal()).
	 * multiply(tmp); } else { BigDecimal p1 = new BigDecimal(new Period(now,
	 * endDate).getDays()); BigDecimal p2 = new BigDecimal(new
	 * Period(interestStartDate, endDate).getDays()); BigDecimal p3 =
	 * transfer.getQuoteContract().getTotalAmount().subtract(transfer.
	 * getQuoteContract().getPrincipal()); actualPrice =
	 * discountRate.multiply(transfer.getQuoteContract().getPrincipal().add(
	 * transfer.getQuoteContract().getUnPayedInterest().subtract(p1.divide(p2.
	 * multiply(p3), 2, RoundingMode.FLOOR)))); } } else if
	 * (endDate.isEqual(now)) { actualPrice =
	 * transfer.getQuoteContract().getPrincipal().add(transfer.getQuoteContract(
	 * ).getUnPayedInterest()).multiply(discountRate); } else { int days = new
	 * Period(new
	 * DateTime(transfer.getQuoteContract().getContractEndDate()).millisOfDay().
	 * withMinimumValue(), now).getDays(); BigDecimal tmp =
	 * BigDecimal.ONE.add(commonService.extRate().divide(new
	 * BigDecimal(100))).multiply(new BigDecimal(days)).divide(new
	 * BigDecimal(365), 2, RoundingMode.FLOOR); actualPrice =
	 * discountRate.multiply(transfer.getQuoteContract().getPrincipal().add(
	 * transfer.getQuoteContract().getUnPayedInterest())).multiply(tmp); }
	 * return actualPrice.setScale(2, RoundingMode.FLOOR); }
	 */
}
