package com.snowstore.pontus.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.snowstore.pontus.domain.Assignee;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.domain.Transfer;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.AssigneeWorkFlow;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.enums.Enums.TransferFlow;
import com.snowstore.pontus.enums.Enums.TransferState;
import com.snowstore.pontus.repository.QuoteContractRepository;
import com.snowstore.pontus.repository.TransferRepository;
import com.snowstore.pontus.service.common.Calendars;
import com.snowstore.pontus.service.vo.SimpleTransferQueryForm;
import com.snowstore.pontus.service.vo.TransferQueryForm;

@Service
@Transactional
public class TransferService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

	@Autowired
	Mapper dozerBeanMapper;

	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private TransferRepository transferRepository;
	@Autowired
	private QuoteContractRepository quoteContractRepository;
	@Autowired
	private CommonService commonService;
	@Autowired
	private AssigneeService assigneeService;
	@Autowired
	private AdminUserService adminUserService;

	public Page<Transfer> findAll(Specification<Transfer> spc, Pageable pageable) {
		return transferRepository.findAll(spc, pageable);
	}

	/**
	 * 撤销转让
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月18日 上午9:22:11
	 * @param transferId
	 */
	public Transfer transferCancel(Long transferId) {
		Transfer transfer = transferRepository.findOne(transferId);
		// 有人预约
		if (transfer.getWorkFlow().equals(Enums.TransferFlow.TRADING.getValue())) {
			throw new PontusServiceException("已有人预约，不能取消!");
		} else if (transfer.getWorkFlow().equals(Enums.TransferFlow.PENDING.getValue())) { // 转让中
			transfer.setState(Enums.TransferState.INVALID.getValue());
			transfer.setWorkFlow(Enums.TransferFlow.CANCEL.getValue());
			QuoteContract quoteContract = quoteContractRepository.findOne(transfer.getQuoteContract().getId());
			quoteContract.setWorkFlow(Enums.QuoteContractWorkFlow.RENEWED.getValue());
			quoteContractRepository.save(quoteContract);
			transfer = transferRepository.save(transfer);
		}
		return transfer;
	}

	/**
	 * 计算转让价格
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月17日 下午3:57:43
	 * @param quoteContract
	 * @param discountRate
	 *            折扣率
	 * @return
	 */
	public BigDecimal getTransferPrice(QuoteContract quoteContract, BigDecimal discountRate) {
		BigDecimal transferPrice;
		OriginalContract orignal = quoteContract.getOriginalContract();
		Date contractEndDate = Calendars.parse(orignal.getContractEndDate(), Calendars.YYYY_MM_DD);
		Date now = Calendars.parse(new Date(), Calendars.YYYY_MM_DD);
		if (now.before(contractEndDate)) {
			if (orignal.getPaybackType().equals(Enums.RepaymentMode.END.getValue())) {
				transferPrice = commonService.quotePriceWhenLtContractEndDateByOnePay(orignal.getInterestStartDate(), orignal.getPrincipal(), orignal.getYearIrr(), discountRate);
			} else {
				transferPrice = commonService.quotePriceWhenLtContractEndDateByManyPay(orignal.getContractEndDate(), orignal.getInterestStartDate(), orignal.getPrincipal(), orignal.getUnPayedInterest(), orignal.getTotalAmount(), discountRate);
			}
		} else if (now.after(contractEndDate)) {
			transferPrice = commonService.quotePriceWhenGtContractEndDate(orignal.getPrincipal().add(orignal.getUnPayedInterest()), discountRate, orignal.getContractEndDate());
		} else {
			transferPrice = commonService.quotePriceWhenEqContractEndDate(orignal.getPrincipal(), orignal.getUnPayedInterest(), discountRate);
		}
		return transferPrice;
	}

	/**
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月17日 下午3:20:49
	 * @param quoteContractId
	 *            挂牌合同id
	 * @param discountRate
	 *            折扣率
	 */
	public void transferSubmit(Long quoteContractId, BigDecimal discountRate) {
		QuoteContract quoteContract = quoteContractRepository.findOne(quoteContractId);
		if (null == quoteContract) {
			LOGGER.error("根据合同id【" + quoteContractId + "】查询挂牌合同表为空");
			throw new PontusServiceException("合同不存在");
		}

		OriginalContract originalContract = quoteContract.getOriginalContract();

		if (null == originalContract || Enums.OriginalContractState.INVALID.getValue().equals(originalContract.getStatus()) || Enums.QuoteContracState.INVALID.getValue().equals(quoteContract.getState())) {
			LOGGER.error("根据挂牌合同id【" + quoteContractId + "】无效，或关联的原始合同为空或无效");
			throw new PontusServiceException("挂牌合同无效,或关联的原始合同为空或无效");
		}
		List<Transfer> transferList = transferRepository.queryByQuoteContractIdAndState(quoteContractId, Enums.TransferState.VALID.getValue());
		if (CollectionUtils.isEmpty(transferList)) {
			BigDecimal tobackAmount = originalContract.getPrincipal().add(originalContract.getUnPayedInterest());
			Transfer transfer = new Transfer();
			transfer.setQuoteContract(quoteContract);
			transfer.setDiscountRate(discountRate);
			transfer.setTransferedValue(commonService.extValue(tobackAmount));
			transfer.setActualPrice(getTransferPrice(quoteContract, discountRate));
			transfer.setCode(sequenceService.nextTransferCode());
			transfer.setExpProfit(commonService.expectProfit(tobackAmount));
			transfer.setEndDate(commonService.getExtDate(quoteContract.getContractEndDate()));
			transferRepository.save(transfer);

			quoteContract.setWorkFlow(Enums.QuoteContractWorkFlow.TRANSFERING.getValue());
			quoteContractRepository.save(quoteContract);

		} else {
			LOGGER.error("根据合同id【" + quoteContractId + "】查询转让表状态为【有效】记录有" + transferList.size() + "条");
			throw new PontusServiceException("转让异常");
		}
	}

	/**
	 * 修改转让状态
	 * 
	 * @date 2016年5月13日
	 * @param id
	 * @return
	 */
	public String changeStatus(Long id) {
		String action = "false";
		try {
			Transfer transfer = transferRepository.findOne(id);
			if (Enums.TransferState.VALID.getValue().equals(transfer.getState())) {// 转让单是否有效
				// 回访取消 ===转让中||交易中|交易确认中
				if (TransferFlow.PENDING.getValue().equals(transfer.getWorkFlow()) || TransferFlow.TRADING.getValue().equals(transfer.getWorkFlow()) || TransferFlow.ENSURE.getValue().equals(transfer.getWorkFlow())) {
					// ①转让单 流程更改为 回访取消 状态更改为无效
					transfer.setWorkFlow(Enums.TransferFlow.REJECT.getValue());
					transfer.setState(TransferState.INVALID.getValue());
					// ②挂牌合同更改状态为已展期
					transferRepository.save(transfer);
					// ②挂牌合同更改状态为已展期
					transfer.getQuoteContract().setWorkFlow(QuoteContractWorkFlow.RENEWED.getValue());
					// ③受让单更改为无效
					for (Assignee assignee : transfer.getTradedAssigneeSet()) {
						assignee.setWorkFlow(AssigneeWorkFlow.REJECT.getValue());
					}
					transferRepository.save(transfer);
					action = "true";
					assigneeService.assigneeSms(transfer.getQuoteContract().getCustomer().getPhone(), transfer.getCode(), "transfer_reject", null);
				} else {
					action = transfer.getWorkFlow();
				}
			} else {
				action = "无效";
			}
		} catch (Exception e) {
			LOGGER.error("修改转让状态：" + e);
		}
		return action;
	}

	/**
	 * 根据id返回转让单
	 * 
	 * @date 2016年5月16日
	 * @param id
	 * @return
	 */
	public Transfer findOne(Long id) {
		return transferRepository.findOne(id);
	}

	/**
	 * 构建查询条件
	 * 
	 * @date 2016年5月13日
	 * @param transferQueryForm
	 * @return
	 */
	public Specification<Transfer> buildSpecification(final TransferQueryForm transferQueryForm) {
		return new Specification<Transfer>() {
			@Override
			public Predicate toPredicate(Root<Transfer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<String> platforms = adminUserService.getUserPlatforms();
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != transferQueryForm.getState()) {// 有效，无效
					list.add(cb.equal(root.<String> get("state"), transferQueryForm.getState()));
				}
				if (null != transferQueryForm.getStatus()) {// 交易时状态
					list.add(cb.equal(root.<String> get("workFlow"), transferQueryForm.getStatus()));
				}
				if (null != transferQueryForm.getApplyTime()) {// 申请时间
					list.add(cb.between(root.<Date> get("createdDate"), transferQueryForm.getApplyFrom(), transferQueryForm.getApplyTo()));
				}
				if (null != transferQueryForm.getQuery()) {// 转让人姓名,转让人电话,转让编号
					list.add(cb.or(cb.like(root.<QuoteContract> get("quoteContract").<Customer> get("customer").<String> get("idCardName"), "%" + transferQueryForm.getQuery() + "%"), cb.like(root.<QuoteContract> get("quoteContract").<Customer> get("customer").<String> get("phone"), "%" + transferQueryForm.getQuery() + "%"),
							cb.like(root.<String> get("code"), "%" + transferQueryForm.getQuery() + "%")));
				}
				if (!platforms.isEmpty()) {
					list.add(root.<QuoteContract> get("quoteContract").<String> get("platform").in(platforms));
				}
				list.add(cb.equal(root.<String> get("state"), TransferState.VALID.getValue()));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		};
	}

	/**
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月13日 下午3:17:53
	 * @param customerId
	 * @param workFlowList
	 * @param orderByList
	 * @param currentPage
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	public List<Transfer> queryTransfer(Long customerId, List<TransferFlow> transferFlows, List<QuoteContractWorkFlow> workFlowList, List<String> orderByList, String transferState, int currentPage, int pageSize) {
		StringBuilder sql = new StringBuilder();
		StringBuilder sbWorkFLow = new StringBuilder();
		StringBuilder sbTransferFlow = new StringBuilder();

		sql.append("select a from Transfer a where a.quoteContract.customer.id = " + customerId);

		if (!CollectionUtils.isEmpty(workFlowList)) {
			for (Iterator<QuoteContractWorkFlow> iterator = workFlowList.iterator(); iterator.hasNext();) {
				QuoteContractWorkFlow quoteContractWorkFlow = iterator.next();
				sbWorkFLow.append("'").append(quoteContractWorkFlow.getValue()).append("',");
			}
			sbWorkFLow.setLength(sbWorkFLow.length() - 1);
			sql.append(" and a.quoteContract.workFlow in(").append(sbWorkFLow.toString()).append(") ");
		}
		
		sql.append(" and a.quoteContract.state in('" + Enums.QuoteContracState.VALID.getValue() + "') ");

		if (!StringUtils.isEmpty(transferState)) {
			sql.append(" and a.state= '").append(transferState).append("' ");
		}
		if (!CollectionUtils.isEmpty(transferFlows)) {
			for (Iterator<TransferFlow> iterator = transferFlows.iterator(); iterator.hasNext();) {
				TransferFlow transferFlow = iterator.next();
				sbTransferFlow.append("'").append(transferFlow.getValue()).append("',");
			}
			sbTransferFlow.setLength(sbTransferFlow.length() - 1);
			sql.append(" and a.workFlow in (").append(sbTransferFlow).append(") ");
		}

		if (!CollectionUtils.isEmpty(orderByList)) {
			StringBuilder sbOrderBy = new StringBuilder();
			for (Iterator<String> iterator = orderByList.iterator(); iterator.hasNext();) {
				String string = iterator.next();
				sbOrderBy.append(" a.").append(string).append(",");
			}
			sbOrderBy.setLength(sbOrderBy.length() - 1);
			sql.append(" order by ").append(sbOrderBy);
		}

		int startPosition = (currentPage - 1) * pageSize;
		List<Transfer> list = em.createQuery(sql.toString()).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
		return list;

	}

	/**
	 * app 交易中心使用，勿修改
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月18日 下午3:36:34
	 * @param customerId
	 * @param transferFlowList
	 * @param orderBy
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryTransfer(String orderBy, int currentPage, int pageSize) {
		StringBuilder sqlBuff = new StringBuilder();
		sqlBuff.append("select t.id,t.WORK_FLOW, ").append("case t.work_flow  when '" + Enums.TransferFlow.SUCCESS.getValue() + "' then  to_char(t.created_Date,'YYYY-MM-DD HH24:MI:SS.ff') else '0' end as tt1, ").append("t.DISCOUNT_RATE, t.actual_Price, t.created_Date, ")
				.append("t.end_Date,t.transfered_Value,t.exp_Profit,t1.id as quoteContractId,t1.CONTRACT_CODE,t1.WORK_FLOW as quoteContractWorkFlow,t2.PRINCIPAL,a.c ").append("from pontus_transfer t ").append("inner join pontus_quote_contract t1 on t.QUOTE_ID = t1.id ").append("inner join pontus_original_contract t2 on t1.CONTRACT_ID = t2.id ")
				.append(" and t2.status not in('" + Enums.OriginalContractState.INVALID.getValue() + "') ").append("left join( select TRANSFER_ID as TRANSFER_ID,count(TRANSFER_ID)as c from pontus_assignee t  group by t.TRANSFER_ID ) a on t.id =TRANSFER_ID ");
		// .append("left join( select TRANSFER_ID as TRANSFER_ID,count(TRANSFER_ID)as c from pontus_assignee t where t.work_flow in('"
		// + AssigneeWorkFlow.APPOINT.getValue() +
		// "') group by t.TRANSFER_ID ) a on t.id =TRANSFER_ID ");
		if (Enums.TransferSortRule.ASSIGNEE_DESC.getValue().equals(orderBy)) {
			// 转让中 交易中
			sqlBuff.append("where t.work_flow in('" + Enums.TransferFlow.PENDING.getValue() + "','" + Enums.TransferFlow.TRADING.getValue() + "') ");
		} else {
			// 转让中 交易中 已交易
			sqlBuff.append("where t.work_flow in('" + Enums.TransferFlow.PENDING.getValue() + "','" + Enums.TransferFlow.TRADING.getValue() + "','" + Enums.TransferFlow.SUCCESS.getValue() + "') ");
		}
		if (Enums.TransferSortRule.DISCOUNTRATE_ASC.getValue().equals(orderBy)) {
			sqlBuff.append("order by tt1 asc,t.DISCOUNT_RATE asc,t.actual_Price asc,t.created_Date asc");
		} else if (Enums.TransferSortRule.DISCOUNTRATE_DESC.getValue().equals(orderBy)) {
			sqlBuff.append("order by tt1 asc,t.DISCOUNT_RATE desc,t.actual_Price asc,t.created_Date asc");
		} else if (Enums.TransferSortRule.TRANSFERPRICE_ASC.getValue().equals(orderBy)) {
			sqlBuff.append("order by tt1 asc,t.actual_Price asc,t.DISCOUNT_RATE asc,t.created_Date asc");
		} else if (Enums.TransferSortRule.ASSIGNEE_DESC.getValue().equals(orderBy)) {
			sqlBuff.append("order by decode(c,null,0,c) desc");
		}

		int startPosition = (currentPage - 1) * pageSize;
		List<Object[]> list = em.createNativeQuery(sqlBuff.toString()).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
		return list;
	}

	public Page<Transfer> queryTransfer(final SimpleTransferQueryForm form, Pageable pageable) {
		return queryTransfer(form, null, pageable);
	}

	public Page<Transfer> queryTransferOrder(final SimpleTransferQueryForm form, Pageable pageable) {
		return queryTransfer(form, null, pageable);
	}

	public List<Transfer> queryTransfer(final SimpleTransferQueryForm form) {
		return transferRepository.findAll(buildSpecification(form, null));
	}

	public Page<Transfer> queryTransfer(SimpleTransferQueryForm form, Long customerId, Pageable pageable) {
		return transferRepository.findAll(buildSpecification(form, customerId), pageable);
	}

	public Page<Transfer> queryTransferOrder(final SimpleTransferQueryForm form, final Long customerId, Pageable pageable) {
		return transferRepository.findAll(new Specification<Transfer>() {
			@Override
			public Predicate toPredicate(Root<Transfer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> p = buildPredicates(root, cb, form, customerId);
				query.orderBy(cb.desc(cb.function("decode", Integer.class, root.get("workFlow"), cb.literal(Enums.TransferFlow.PENDING.getValue()), cb.literal(4), cb.literal(Enums.TransferFlow.TRADING.getValue()), cb.literal(3), cb.literal(Enums.TransferFlow.ENSURE.getValue()), cb.literal(3), cb.literal(Enums.TransferFlow.SUCCESS.getValue()), cb.literal(2), cb.literal(1))),
						cb.desc(root.get("createdDate")));
				return cb.and(p.toArray(new Predicate[p.size()]));
			}
		}, pageable);
	}

	public Specification<Transfer> buildSpecification(final SimpleTransferQueryForm form, final Long customerId) {
		return new Specification<Transfer>() {
			@Override
			public Predicate toPredicate(Root<Transfer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> p = buildPredicates(root, cb, form, customerId);
				return cb.and(p.toArray(new Predicate[p.size()]));
			}
		};
	}

	private List<Predicate> buildPredicates(Root<Transfer> root, CriteriaBuilder cb, final SimpleTransferQueryForm form, final Long customerId) {
		List<Predicate> p = new ArrayList<>();
		if (customerId != null) {
			p.add(cb.equal(root.get("quoteContract").get("customer"), customerId));
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(form.getState())) {
			p.add(cb.equal(root.get("state"), form.getState()));
		}
		if (form.getWorkFlow() != null && !form.getWorkFlow().isEmpty()) {
			p.add(root.get("workFlow").in(form.getWorkFlow()));
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(form.getPlatform())) {
			p.add(cb.equal(root.get("quoteContract").get("platform"), form.getPlatform()));
		}
		if (form.getDiscountRateBegin() != null) {
			p.add(cb.ge(root.<BigDecimal> get("discountRate"), form.getDiscountRateBegin()));
		}
		if (form.getDiscountRateEnd() != null) {
			p.add(cb.le(root.<BigDecimal> get("discountRate"), form.getDiscountRateEnd()));
		}
		if (form.getRemainingTimeBegin() != null) {
			DateTime beginTime = new DateTime().plusDays(form.getRemainingTimeBegin()).millisOfDay().withMinimumValue();
			p.add(cb.greaterThanOrEqualTo(root.<Date> get("endDate"), beginTime.toDate()));
		}
		if (form.getRemainingTimeEnd() != null) {
			DateTime endTime = new DateTime().plusDays(form.getRemainingTimeEnd()).millisOfDay().withMaximumValue();
			p.add(cb.lessThanOrEqualTo(root.<Date> get("endDate"), endTime.toDate()));
		}
		return p;
	}

	public Transfer save(Transfer transfer) {
		return transferRepository.save(transfer);
	}

	public Long countTransferRecord(Long userId) {
		return transferRepository.countTransferRecord(userId);
	}
}
