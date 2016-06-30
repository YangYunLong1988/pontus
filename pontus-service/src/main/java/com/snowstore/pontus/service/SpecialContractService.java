package com.snowstore.pontus.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.pontus.domain.Assignee;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.domain.SpecialContract;
import com.snowstore.pontus.domain.Transfer;
import com.snowstore.pontus.enums.Enums.AssigneeWorkFlow;
import com.snowstore.pontus.enums.Enums.OriginalContractState;
import com.snowstore.pontus.enums.Enums.QuoteContracState;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.enums.Enums.TransferState;
import com.snowstore.pontus.repository.OriginalContractRepository;
import com.snowstore.pontus.repository.QuoteContractRepository;
import com.snowstore.pontus.repository.SpecialContractRepository;
import com.snowstore.pontus.repository.TransferRepository;
import com.snowstore.pontus.service.vo.SpecialContractQueryForm;

@Service
@Transactional
public class SpecialContractService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpecialContractService.class);

	@Autowired
	private SpecialContractRepository specialContractRepository;

	@Autowired
	private QuoteContractRepository quoteContractRepository;

	@Autowired
	private TransferRepository transferRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private OriginalContractRepository originalContractRepository;

	public Page<SpecialContract> findAll(Specification<SpecialContract> spc, Pageable pageable) {
		Page<SpecialContract> page = specialContractRepository.findAll(spc, pageable);
		LOGGER.info("挂牌合同查询,共{}条", page.getTotalElements());
		return page;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findAll(Long customerId, int currentPage, int pageSize) {
		StringBuilder sqlBuff = new StringBuilder();
		sqlBuff.append("select a,b,c from SpecialContract a ,OriginalContract b ,QuoteContract c where a.originalContract.id = b.id and c.originalContract.id = b.id and c.customer.id = " + customerId);

		sqlBuff.append(" order by  a.createdDate desc");

		int startPosition = (currentPage - 1) * pageSize;
		List<Object[]> list = em.createQuery(sqlBuff.toString()).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
		return list;
	}

	public Specification<SpecialContract> buildSpecification(final Long customerId) {
		return new Specification<SpecialContract>() {
			@Override
			public Predicate toPredicate(Root<SpecialContract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.<OriginalContract> get("originalContract").<QuoteContract> get("quoteContract").<Customer> get("customer").<Long> get("id"), customerId));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		};

	}

	/**
	 * 根据id查询特殊兑付
	 * 
	 * @date 2016年6月17日
	 * @param id
	 * @return
	 */
	public SpecialContract findOne(Long id) {
		return specialContractRepository.findOne(id);
	}

	public Specification<SpecialContract> buildSpecification(final SpecialContractQueryForm specialContractQueryForm) {
		return new Specification<SpecialContract>() {
			@Override
			public Predicate toPredicate(Root<SpecialContract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				List<String> platforms = new ArrayList<String>();// adminUserService.getUserPlatforms();
				if (null != specialContractQueryForm.getPlatform()) {
					list.add(cb.equal(root.<OriginalContract> get("originalContract").<String> get("platform"), specialContractQueryForm.getPlatform()));
				} else if (!platforms.isEmpty()) {
					list.add(root.<OriginalContract> get("originalContract").<String> get("platform").in(platforms));
				}

				if (null != specialContractQueryForm.getQuery()) {
					list.add(cb.or(cb.like(root.<OriginalContract> get("originalContract").<String> get("investorName"), '%' + specialContractQueryForm.getQuery() + '%'), cb.like(root.<OriginalContract> get("originalContract").<String> get("contractCode"), '%' + specialContractQueryForm.getQuery() + '%'),
							cb.like(root.<OriginalContract> get("originalContract").<String> get("mobile"), '%' + specialContractQueryForm.getQuery() + '%')));
				}
				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		};

	}

	public SpecialContract get(Long id) {
		return specialContractRepository.findOne(id);
	}

	/**
	 * 新建特殊回购流程逻辑 ①未挂牌 原始合同设置为无效 ②已挂牌 交易成功 直接返回 false 操作失败 ③合同处于转让中 执行撤销转让 原始合同设置为无效 挂牌合同设置为无效 ④挂牌合同为其他状态 原始合同设置为无效 挂牌合同设置为无效
	 * 
	 * @param original
	 */
	public Boolean newSpecialContractProcess(OriginalContract original, Map<String,String> emailMap) {
		QuoteContract quoteContract = quoteContractRepository.findByContractCode(original.getContractCode());
		if (null == quoteContract) {
			// 未挂牌
		} else if (QuoteContractWorkFlow.SUCCESS.getValue().equals(quoteContract.getWorkFlow())) {
			// 已挂牌 交易成功 拒绝执行
			return false;
		} else if (QuoteContractWorkFlow.TRANSFERING.getValue().equals(quoteContract.getWorkFlow())) {
			// 转让中
			List<Transfer> transfers = transferRepository.queryByQuoteContractIdAndState(quoteContract.getId(), TransferState.VALID.getValue());
			for (Transfer transfer : transfers) {
				transfer.setState(TransferState.INVALID.getValue());
				for (Assignee assignee : transfer.getTradedAssigneeSet()) {
					if (AssigneeWorkFlow.AGREE.getValue().equals(assignee.getWorkFlow())) {// 状态为回访同意
						emailMap.put(assignee.getCode(), assignee.getCustomer().getPhone());
					}
					assignee.setWorkFlow(AssigneeWorkFlow.REJECT.getValue());
				}
				transferRepository.save(transfer);
			}

		}
		// 原始合同设置为无效
		original.setStatus(OriginalContractState.INVALID.getValue());
		originalContractRepository.save(original);
		if (null != quoteContract) {
			// 挂牌合同设置为无效
			quoteContract.setState(QuoteContracState.INVALID.getValue());
			quoteContractRepository.save(quoteContract);
		}
		return true;

	}

	public SpecialContract findByOriginalContract(OriginalContract originalContract) {
		return specialContractRepository.findByOriginalContract(originalContract);
	}

	public Page<SpecialContract> findAll(final List<Long> contractIds, PageRequest pageable) {
		if (contractIds.isEmpty()) {
			return null;
		}
		return specialContractRepository.findAll(new Specification<SpecialContract>() {
			@Override
			public Predicate toPredicate(Root<SpecialContract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> p = new ArrayList<Predicate>();
				p.add(root.get("originalContract").in(contractIds));
				return cb.and(p.toArray(new Predicate[p.size()]));
			}
		}, pageable);
	}

}
