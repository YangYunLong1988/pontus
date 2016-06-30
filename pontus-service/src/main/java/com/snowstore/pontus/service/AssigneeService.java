package com.snowstore.pontus.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.snowstore.pontus.domain.Assignee;
import com.snowstore.pontus.domain.AssigneeAttachment;
import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.Transfer;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.AssigneeWorkAttachFlow;
import com.snowstore.pontus.enums.Enums.AssigneeWorkFlow;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.enums.Enums.TransferFlow;
import com.snowstore.pontus.repository.AssigneeRepository;
import com.snowstore.pontus.repository.BankInfoRepository;
import com.snowstore.pontus.repository.CustomerRepository;
import com.snowstore.pontus.repository.TransferRepository;
import com.snowstore.pontus.service.datagram.EsbHelper;
import com.snowstore.pontus.service.vo.AssigneeQueryForm;
import com.snowstore.pontus.service.vo.Result;

@Service
@Transactional
public class AssigneeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssigneeService.class);
	@Autowired
	private AssigneeRepository assigneeRepository;

	@Autowired
	private TransferRepository transferRepository;
	@Autowired
	private BankInfoRepository bankInfoRepository;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	Mapper dozerBeanMapper;
	@Autowired
	private CustomerRepository customerRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private EsbHelper esbHelper;
	@Autowired
	private AssigneeAttachmentService assigneeAttachmentService;
	private static ResourceBundle bundle = ResourceBundle.getBundle("sms");

	public void assigneeSubmit(Long transferId, Long bankInfoId, Long customerId) {
		BankInfo bankInfo = bankInfoRepository.findOne(bankInfoId);
		Customer customer = customerRepository.findOne(customerId);
		if (null != bankInfo) {
			if (!bankInfo.getCustomer().getId().equals(customerId)) {
				LOGGER.error("预约的银行卡【" + bankInfoId + "】不属于该用户");
				throw new PontusServiceException("预约的银行卡不属于该用户");
			}

			// 校验用户是否之前受让过
			if (!CollectionUtils.isEmpty(assigneeRepository.findByTransferAndCustommer(transferId, customerId))) {
				throw new PontusServiceException("此资产只允许预约一次!");
			}

			Transfer transfer = transferRepository.findOne(transferId);
			// 自己转让的资产不能预约
			if (transfer.getQuoteContract().getCustomer().getId().longValue() == customerId.longValue()) {
				throw new PontusServiceException("不能预约自己转让的资产!");
			}

			transfer.setWorkFlow(Enums.TransferFlow.TRADING.getValue());
			transferRepository.save(transfer);

			Assignee assignee = new Assignee();
			assignee.setState(Enums.AssigneeState.VALID.getValue());
			assignee.setWorkFlow(Enums.AssigneeWorkFlow.APPOINT.getValue());
			assignee.setCode(sequenceService.nextAssigneeCode());
			assignee.setAssigneePrice(transfer.getActualPrice());
			assignee.setCustomer(customer);
			assignee.setBankInfo(bankInfo);
			assignee.setTransfer(transfer);
			assigneeRepository.save(assignee);

		} else {
			LOGGER.error("预约的银行卡【" + bankInfoId + "】不存在");
			throw new PontusServiceException("预约的银行卡不存在");
		}
	}

	@SuppressWarnings("unchecked")
	public Map<Long, String> countByTransferIds(String transferIds) {
		Map<Long, String> ret = new HashMap<Long, String>();
		// String sql = "select TRANSFER_ID,count(TRANSFER_ID) from
		// pontus_assignee t where t.TRANSFER_ID in(" + transferIds + ") and
		// t.work_flow in('" + AssigneeWorkFlow.APPOINT.getValue() + "') group
		// by t.TRANSFER_ID";
		String sql = "select TRANSFER_ID,count(TRANSFER_ID) from pontus_assignee t where t.TRANSFER_ID in(" + transferIds + ")  group by t.TRANSFER_ID";
		List<Object[]> list = em.createNativeQuery(sql).getResultList();
		if (null != list && 0 < list.size()) {
			for (int i = 0; i < list.size(); i++) {
				Object[] tmp = list.get(i);
				ret.put(Long.valueOf(String.valueOf(tmp[0])), String.valueOf(tmp[1]));
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> queryByAssigneeWorkFlowAndCustomerId(Long customerId, List<String> assigneeWorkFlowList, int currentPage, int pageSize) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select t.id,t.state,t.customer_id,t.transfer_id,t.attach_flow,t.code,t.work_flow,t.assignee_price,t1.DISCOUNT_RATE,t1.EXP_PROFIT,t1.END_DATE,t2.contract_Code,t2.principal, ").append("case t.work_flow  when '交易完成' then '999'  else '1' end as t1 ,")
				.append(" t2.un_Payed_Interest,t3.id as quoteId  ").append("from pontus_assignee t,pontus_transfer t1,pontus_quote_contract t3,pontus_original_contract t2 ").append("where t.transfer_id = t1.id and t1.QUOTE_ID = t3.ID and t3.CONTRACT_ID = t2.id ")
				.append(" and t.customer_id = '" + customerId + "' ").append(" and t3.state in('" + Enums.QuoteContracState.VALID.getValue()+ "') ");
		if (!CollectionUtils.isEmpty(assigneeWorkFlowList)) {
			StringBuilder workFlowBuilder = new StringBuilder();
			for (Iterator<String> iterator = assigneeWorkFlowList.iterator(); iterator.hasNext();) {
				workFlowBuilder.append("'").append(iterator.next()).append("',");
			}
			workFlowBuilder.setLength(workFlowBuilder.length() - 1);
			sqlBuilder.append(" and t.work_flow in(").append(workFlowBuilder).append(") ");
		}

		sqlBuilder.append(" order by t1 desc,t.ID desc ");

		int startPosition = (currentPage - 1) * pageSize;
		List<Object[]> objs = em.createNativeQuery(sqlBuilder.toString()).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
		return objs;
	}

	/**
	 * 分页查询
	 * 
	 * @date 2016年5月16日
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<Assignee> findAll(Specification<Assignee> spec, Pageable pageable) {
		return assigneeRepository.findAll(spec, pageable);
	}

	public Page<Assignee> findAllOrder(final AssigneeQueryForm assigneeQueryForm, final Long customerId, Pageable pageable) {
		return assigneeRepository.findAll(new Specification<Assignee>() {
			@Override
			public Predicate toPredicate(Root<Assignee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = buildPredicates(root, cb, assigneeQueryForm, customerId);
				query.orderBy(cb.desc(cb.function("decode", Integer.class, root.get("workFlow"), cb.literal(AssigneeWorkFlow.REJECT.getValue()), cb.literal(4), cb.literal(AssigneeWorkFlow.APPOINT.getValue()), cb.literal(3), cb.literal(AssigneeWorkFlow.AGREE.getValue()),
						cb.literal(3), cb.literal(AssigneeWorkFlow.SUCCESS.getValue()), cb.literal(2), cb.literal(1))), cb.desc(root.get("createdDate")));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		}, pageable);
	}

	public Specification<Assignee> buildSpecification(final AssigneeQueryForm assigneeQueryForm) {
		return buildSpecification(assigneeQueryForm, null);
	}

	public Specification<Assignee> buildSpecification(final AssigneeQueryForm assigneeQueryForm, final Long customerId) {
		return new Specification<Assignee>() {
			@Override
			public Predicate toPredicate(Root<Assignee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = buildPredicates(root, cb, assigneeQueryForm, customerId);
				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		};
	}

	public List<Predicate> buildPredicates(Root<Assignee> root, CriteriaBuilder cb, AssigneeQueryForm assigneeQueryForm, final Long customerId) {
		List<Predicate> list = new ArrayList<Predicate>();
		if (null != customerId) {// 转让单id
			list.add(cb.equal(root.get("customer"), customerId));
		}
		if (null != assigneeQueryForm.getTransferId()) {// 转让单id
			list.add(cb.equal(root.join("transfer").get("id").as(Long.class), assigneeQueryForm.getTransferId()));
		}
		if (null != assigneeQueryForm.getState()) { // 客户登记,回访同意,回访取消,交易完成
			list.add(cb.equal(root.<String> get("workFlow"), assigneeQueryForm.getState()));
		}
		if (null != assigneeQueryForm.getApplyTime()) {// 申请时间
			list.add(cb.between(root.<Date> get("createdDate"), assigneeQueryForm.getApplyFrom(), assigneeQueryForm.getApplyTo()));
		}
		if (null != assigneeQueryForm.getQuery()) {// 受让人姓名,受让编号,受让人手机号码
			list.add(cb.or(cb.like(root.join("customer").get("idCardName").as(String.class), "%" + assigneeQueryForm.getQuery() + "%"), cb.like(root.<String> get("code"), "%" + assigneeQueryForm.getQuery() + "%"),
					cb.like(root.join("customer").get("phone").as(String.class), "%" + assigneeQueryForm.getQuery() + "%")));
		}
		return list;
	}

	/**
	 * 修改受让状态
	 * 
	 * @date 2016年5月16日
	 * @param id
	 * @param workFlow
	 * @return
	 */
	public String changeWorkFlow(Long id, String workFlow) {
		String action = "false";
		if (Enums.isValidEnumValue(Enums.AssigneeWorkFlow.class, workFlow)) {// 验证枚举
			try {
				Assignee assignee = assigneeRepository.findOne(id);
				Transfer transfer = assignee.getTransfer();
				if (Enums.TransferState.VALID.getValue().equals(transfer.getState())) {
					if (AssigneeWorkFlow.AGREE.getValue().equals(workFlow)) {// 同意回访
						if (transfer.getWorkFlow().equals(Enums.TransferFlow.TRADING.getValue())) {// 交易中的订单才可以同意回访
							if (assignee.getWorkFlow().equals(Enums.AssigneeWorkFlow.APPOINT.getValue())) {// 预约状态是客户登记
								// 修改预约状态和附件状态
								assignee.setWorkFlow(Enums.AssigneeWorkFlow.AGREE.getValue());
								assignee.setAttachFlow(Enums.AssigneeWorkAttachFlow.UNDO.getValue());// 附件状态
								// 修改转让单状态-交易确认中
								transfer.setWorkFlow(Enums.TransferFlow.ENSURE.getValue());
								transferRepository.save(transfer);
								action = "true";
								assigneeSms(transfer.getQuoteContract().getCustomer().getPhone(), transfer.getCode(), "assignee_agree_zrf", null);// 转让让人
								assigneeSms(assignee.getCustomer().getPhone(), assignee.getCode(), "assignee_agree_srf", null);// 受让人
							} else {
								action = "预约状态为 " + assignee.getWorkFlow() + " 操作失败";
							}
						} else {
							action = "转让单状态为 " + transfer.getWorkFlow() + " 操作失败";
						}
					} else if (Enums.AssigneeWorkFlow.REJECT.getValue().equals(workFlow)) {// 回访取消
						if (transfer.getWorkFlow().equals(Enums.TransferFlow.TRADING.getValue()) || transfer.getWorkFlow().equals(Enums.TransferFlow.ENSURE.getValue())) {// 交易中或交易确认中订单才可以取消回访
							if (assignee.getWorkFlow().equals(Enums.AssigneeWorkFlow.AGREE.getValue()) || assignee.getWorkFlow().equals(Enums.AssigneeWorkFlow.APPOINT.getValue())) {// 预约状态
								// 修改预约状态
								assignee.setWorkFlow(Enums.AssigneeWorkFlow.REJECT.getValue());
								// 修改转让单--交易中
								transfer.setWorkFlow(Enums.TransferFlow.TRADING.getValue());
								transferRepository.save(transfer);
								action = "true";
								assigneeSms(assignee.getCustomer().getPhone(), assignee.getCode(), "assignee_reject", null);// 受让人
							} else {
								action = "预约状态为 " + assignee.getWorkFlow() + " 操作失败";
							}
						} else {
							action = "转让单状态为 " + transfer.getWorkFlow() + " 操作失败";
						}
					}
				} else {
					action = "无效的转让单";
				}
			} catch (Exception e) {
				LOGGER.error("修改受让状态失败：" + e);
			}
		}
		LOGGER.info("修改状态结果：" + action);
		return action;
	}

	public List<Assignee> findByTransferAndCustommer(Long id, Long customerId) {
		return assigneeRepository.findByTransferAndCustommer(id, customerId);
	}

	/***
	 * 审核附件
	 * 
	 * @date 2016年5月20日
	 * @return
	 */
	public Result<String> assigneeAttachment(Long assigneeId, boolean opinion, String reason) {
		Result<String> result = new Result<String>(Result.Type.FAILURE);
		Assignee assignee = assigneeRepository.findOne(assigneeId);
		Transfer transfer = assignee.getTransfer();
		// 判断转让单状态
		if (!Enums.TransferFlow.ENSURE.getValue().equals(transfer.getWorkFlow()) && Enums.TransferState.INVALID.getValue().equals(transfer.getState())) {
			result.addMessage("转让单当前状态" + transfer.getWorkFlow() + ",审核失败!");
			return result;
		}
		if (opinion) { // 同意
			if (assignee.getWorkFlow().equals(AssigneeWorkFlow.AGREE.getValue())) {// 当前预约为回访同意
				// 附件状态 -待审核或审核回退
				if (assignee.getAttachFlow().equals(AssigneeWorkAttachFlow.PENDING.getValue()) || assignee.getAttachFlow().equals(AssigneeWorkAttachFlow.REJECT.getValue())) {
					// ①修改预约状态
					for (Assignee se : transfer.getTradedAssigneeSet()) {
						if (se.equals(assignee)) {// 如果预约人是当前要审核的人
							se.setWorkFlow(AssigneeWorkFlow.SUCCESS.getValue());
							se.setAttachFlow(AssigneeWorkAttachFlow.SUCESS.getValue());
						} else {// 其余的状态修改--回访取消
							se.setWorkFlow(AssigneeWorkFlow.REJECT.getValue());
						}
					}
					// ②设置挂牌资产工作流 为交易成功
					transfer.getQuoteContract().setWorkFlow(QuoteContractWorkFlow.SUCCESS.getValue());
					// ③交易单更新状态为 交易成功
					transfer.setWorkFlow(TransferFlow.SUCCESS.getValue());
					transfer.setTradedTime(new Date());
					transfer = transferRepository.save(transfer);
					result.setType(Result.Type.SUCCESS);
					// 通知转让方和受让方
					assigneeSms(transfer.getQuoteContract().getCustomer().getPhone(), transfer.getCode(), "assignee_success_zrf", null);// 转让人
					assigneeSms(assignee.getCustomer().getPhone(), assignee.getCode(), "assignee_success_srf", null);// 受让人
				} else {
					result.addMessage("预约附件状态" + assignee.getAttachFlow() + ",审核失败!");
				}
			} else {
				result.addMessage("预约当前状态" + assignee.getWorkFlow() + ",审核失败!");
			}
		} else {
			// 预约状态是-同意回访
			if (assignee.getWorkFlow().equals(AssigneeWorkFlow.AGREE.getValue())) {
				// 附件状态是-待审核或审核回退
				if (assignee.getAttachFlow().equals(AssigneeWorkAttachFlow.PENDING.getValue()) || assignee.getAttachFlow().equals(AssigneeWorkAttachFlow.REJECT.getValue())) {
					assignee.setAttachFlow(AssigneeWorkAttachFlow.REJECT.getValue());
					assignee = assigneeRepository.save(assignee);
					List<AssigneeAttachment> listattachment = assigneeAttachmentService.getByAssigneeId(assignee.getId());
					for (AssigneeAttachment attachment : listattachment) {
						attachment.setAttachType(Enums.AssigneeAttachmentState.INVALID.getValue());
						assigneeAttachmentService.save(attachment);
					}
					result.setType(Result.Type.SUCCESS);
					// 通知受让方，重新上传附件
					assigneeSms(assignee.getCustomer().getPhone(), assignee.getCode(), "assignee_attach_reject", reason);// 受让人
				} else {
					result.addMessage("预约附件状态" + assignee.getAttachFlow() + ",审核失败!");
				}
			} else {
				result.addMessage("预约当前状态" + assignee.getWorkFlow() + ",审核失败!");
			}
		}
		return result;
	}

	// 审核成功后发送短信
	public void assigneeSms(String phone, String code, String templatekey, String reason) {
		try {
			// 获取短信模板
			String template = bundle.getString(templatekey);
			String value=new String(template.getBytes("ISO-8859-1"),"UTF-8");
			template = MessageFormat.format(value, code, reason);
			// 通知受让方
			esbHelper.sendSmsRequest(template, phone, false);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public Long countTransfeeRecord(Long userId) {
		return assigneeRepository.countTransfeeRecord(userId);
	}
}
