package com.snowstore.pontus.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.domain.RenewalAgreement;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.OriginalContractWorkFlow;
import com.snowstore.pontus.enums.Enums.QuoteContracState;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.repository.CustomerRepository;
import com.snowstore.pontus.repository.OriginalContractRepository;
import com.snowstore.pontus.repository.QuoteContractRepository;
import com.snowstore.pontus.service.datagram.EsbHelper;
import com.snowstore.pontus.service.vo.AddQuoteContractForm;
import com.snowstore.pontus.service.vo.AssetRenewal;
import com.snowstore.pontus.service.vo.ContractQueryForm;
import com.snowstore.pontus.service.vo.CreateQuoteContractForm;
import com.snowstore.pontus.service.vo.ExtFileImageVo;
import com.snowstore.pontus.service.vo.QuoteContractForm;
import com.snowstore.pontus.service.vo.Result;

@Service
@Transactional
public class QuoteContractService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteContractService.class);

	@Autowired
	private OriginalContractRepository originalContractRepository;
	@Autowired
	private QuoteContractRepository quoteContractRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	Mapper dozerBeanMapper;
	@Autowired
	private EsbHelper esbHelper;
	@Autowired
	private ContractService contractService;
	@Autowired
	private RenewalAgreementService extFileService;
	@Autowired
	private ExtendFileImageService extendFileImageService;
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private AdminUserService adminUserService;

	public QuoteContract findOne(Long id) {
		return quoteContractRepository.findOne(id);
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
	public List<QuoteContract> queryQuoteContract(Long customerId, List<Enums.QuoteContractWorkFlow> workFlowList, List<String> orderByList, int currentPage, int pageSize) {
		StringBuilder sql = new StringBuilder();
		if (null != workFlowList && 0 < workFlowList.size()) {
			StringBuilder sbWorkFLow = new StringBuilder();
			for (Iterator<QuoteContractWorkFlow> iterator = workFlowList.iterator(); iterator.hasNext();) {
				QuoteContractWorkFlow quoteContractWorkFlow = iterator.next();
				sbWorkFLow.append("'").append(quoteContractWorkFlow.getValue()).append("',");
			}
			sbWorkFLow.setLength(sbWorkFLow.length() - 1);

			sql.append("select b from OriginalContract a ,QuoteContract b where a.id = b.originalContract.id and b.workFlow in(").append(sbWorkFLow.toString()).append(") ");
			sql.append(" and b.customer.id =").append(customerId).append(" ").append(" and a.status not in('").append(Enums.OriginalContractState.INVALID.getValue()).append("') ");

			if (!CollectionUtils.isEmpty(orderByList)) {
				StringBuilder sbOrderBy = new StringBuilder();
				for (Iterator<String> iterator = orderByList.iterator(); iterator.hasNext();) {
					String string = iterator.next();
					sbOrderBy.append(" a.").append(string).append(",");
				}
				sbOrderBy.setLength(sbOrderBy.length() - 1);
				sql.append(" order by ").append(sbOrderBy).append(" desc");
			}

			int startPosition = (currentPage - 1) * pageSize;
			List<QuoteContract> list = em.createQuery(sql.toString()).setFirstResult(startPosition).setMaxResults(pageSize).getResultList();
			return list;
		} else {
			return null;
		}

	}

	public int updateWorkFlow(Long id, String workFlow) {
		return quoteContractRepository.updateWorkFlow(id, workFlow);
	}

	public Page<QuoteContract> findAll(Specification<QuoteContract> spc, Pageable pageable) {
		Page<QuoteContract> page = quoteContractRepository.findAll(spc, pageable);
		LOGGER.info("挂牌合同查询,共{}条", page.getTotalElements());
		return page;
	}

	public Page<QuoteContract> findAll(final List<String> workflow, final Long customerId, Pageable pageable) {
		return quoteContractRepository.findAll(new Specification<QuoteContract>() {
			@Override
			public Predicate toPredicate(Root<QuoteContract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> p = new ArrayList<Predicate>();
				p.add(root.get("workFlow").in(workflow));
				p.add(cb.equal(root.get("state"), Enums.QuoteContracState.VALID.getValue()));
				p.add(cb.equal(root.get("customer"), customerId));
				return cb.and(p.toArray(new Predicate[p.size()]));
			}
		}, pageable);
	}

	public Page<QuoteContract> findAll(ContractQueryForm contractQueryForm, Pageable pageable) {
		Page<QuoteContract> page = quoteContractRepository.findAll(buildSpecification(contractQueryForm), pageable);
		LOGGER.info("挂牌合同查询,共{}条", page.getTotalElements());
		return page;
	}

	public Page<QuoteContract> queryQuoteContractList(QuoteContractForm contractForm) {
		return quoteContractRepository.findAll(buildSpecification(contractForm), contractForm);
	}
	
	public Page<QuoteContract> queryQuoteContractListAndNotInState(QuoteContractForm contractForm,String state) {
		return quoteContractRepository.findAll(buildSpecification(contractForm,state), contractForm);
	}

	public List<QuoteContract> findByContractCodeAndCustomerIdAndWorkFlows(Long customerId, String contractCode, List<String> workFlows) {
		return quoteContractRepository.findByContractCodeAndCustomerIdAndWorkFlows(contractCode, customerId, workFlows);
	}

	public Long saveQuoteContract(AddQuoteContractForm addQuoteContractForm) {

		// 过滤原始合同是否存在，根据 合同编号 证件号码 购买平台
		OriginalContract originalContract = originalContractRepository.findByContractCodeAndPlatformAndCertiNo(addQuoteContractForm.getContractCode(), addQuoteContractForm.getPlatform(), customerRepository.findOne(addQuoteContractForm.getCustomerId()).getIdCardAccount());
		if (null == originalContract || Enums.OriginalContractState.INVALID.getValue().equals(originalContract.getStatus())) {
			throw new PontusServiceException("对不起，没有匹配到您的合同，请您认真核查后再次提交，谢谢。");
		}

		// 过滤 挂牌合同
		List<String> workFlows = new ArrayList<String>();
		workFlows.add(Enums.QuoteContractWorkFlow.PASSED.getValue());
		workFlows.add(Enums.QuoteContractWorkFlow.RENEWED.getValue());
		workFlows.add(Enums.QuoteContractWorkFlow.TRANSFERING.getValue());
		workFlows.add(Enums.QuoteContractWorkFlow.SUCCESS.getValue());
		List<QuoteContract> quoteContracts = quoteContractRepository.findByContractCodeAndWorkFlows(addQuoteContractForm.getContractCode(), workFlows);
		if (quoteContracts.size() > 0) {
			throw new PontusServiceException("合同已存在!");
		}

		List<String> workFlowNew = new ArrayList<String>();
		workFlowNew.add(Enums.QuoteContractWorkFlow.NEW.getValue());
		List<QuoteContract> quoteContractNews = quoteContractRepository.findByContractCodeAndCustomerIdAndWorkFlows(addQuoteContractForm.getContractCode(), addQuoteContractForm.getCustomerId(), workFlowNew);

		QuoteContract quoteContract = null;
		if (null != quoteContractNews && 0 < quoteContractNews.size()) {
			quoteContract = quoteContractNews.get(0);
		} else {
			quoteContract = new QuoteContract();
		}

		dozerBeanMapper.map(addQuoteContractForm, quoteContract);
		quoteContract.setCustomer(customerRepository.findOne(addQuoteContractForm.getCustomerId()));
		quoteContractRepository.save(quoteContract);
		return quoteContract.getId();

	}

	public Long updateQuoteContract(AddQuoteContractForm form, Long customerId, Long contractId) {
		QuoteContract newContract = quoteContractRepository.findOne(contractId);
		dozerBeanMapper.map(form, newContract);
		newContract.setCustomer(customerRepository.findOne(customerId));
		newContract.setWorkFlow(Enums.QuoteContractWorkFlow.PENDING.getValue());
		quoteContractRepository.save(newContract);
		return newContract.getId();

	}

	public Specification<QuoteContract> buildSpecification(final ContractQueryForm contractQueryForm) {
		return new Specification<QuoteContract>() {
			@Override
			public Predicate toPredicate(Root<QuoteContract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				list.add(cb.equal(root.get("state"), Enums.QuoteContracState.VALID.getValue()));

				if (null != contractQueryForm.getCustomerId()) {
					list.add(cb.equal(root.get("customer"), contractQueryForm.getCustomerId()));
				}
				if (null != contractQueryForm.getWorkFlow()) {
					list.add(root.<String> get("workFlow").in(Arrays.asList(contractQueryForm.getWorkFlow().split(","))));
				}

				List<String> platforms = adminUserService.getUserPlatforms();
				if (null != contractQueryForm.getProductResource()) {
					list.add(cb.equal(root.<String> get("platform"), contractQueryForm.getProductResource()));
				} else if (!platforms.isEmpty()) {
					list.add(root.<String> get("platform").in(platforms));
				}
				if (null != contractQueryForm.getApplyDate()) {
					list.add(cb.between(root.<Date> get("createdDate"), contractQueryForm.getApplyDateFrom(), contractQueryForm.getApplyDateTo()));
				}
				//挂牌合同 查找有效合同
				list.add(cb.equal(root.<String> get("state"), QuoteContracState.VALID.getValue()));
				if (null != contractQueryForm.getQuery()) {
					list.add(cb.or(cb.like(root.<String> get("contractCode"), '%' + contractQueryForm.getQuery() + '%'), cb.like(root.<String> get("productName"), '%' + contractQueryForm.getQuery() + '%'), cb.like(root.<Customer> get("customer").<String> get("idCardAccount"), '%' + contractQueryForm.getQuery() + '%'), cb.like(root.<Customer> get("customer").<String> get("idCardName"), '%' + contractQueryForm.getQuery() + '%')));
				}
				if (null != contractQueryForm.getQueryAgreement()) {// 合同来源,协议名称,协议编号,拥有人
					list.add(cb.or(cb.like(root.<String> get("platform"), '%' + contractQueryForm.getQueryAgreement() + '%'), cb.like(root.join("renewalAgreement").get("fileName").as(String.class), '%' + contractQueryForm.getQueryAgreement() + '%'), cb.like(root.join("renewalAgreement").get("objectId").as(String.class), '%' + contractQueryForm.getQueryAgreement() + '%'),
							cb.like(root.join("customer").get("idCardName").as(String.class), '%' + contractQueryForm.getQueryAgreement() + '%')));
				}
				if (null != contractQueryForm.getPaybackType()) {
					list.add(cb.equal(root.get("paybackType"), contractQueryForm.getPaybackType()));
				}
				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		};

	}

	public QuoteContract save(CreateQuoteContractForm form) {
		QuoteContract quoteContract = new QuoteContract();
		try {
			BeanUtils.copyProperties(quoteContract, form);
			return quoteContractRepository.save(quoteContract);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new PontusServiceException("保存 QuoteContract 异常", e);
		}
	}

	public QuoteContract get(Long id) {
		return quoteContractRepository.findOne(id);
	}

	public QuoteContract saveOrUpdate(QuoteContract quoteContract) {
		return quoteContractRepository.save(quoteContract);
	}

	private Specification<QuoteContract> buildSpecification(final QuoteContractForm contractForm) {
		return new Specification<QuoteContract>() {
			@Override
			public Predicate toPredicate(Root<QuoteContract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != contractForm.getWorkFlowList()) {
					List<String> workFlows = new ArrayList<String>();
					for (int i = 0; i < contractForm.getWorkFlowList().size(); i++) {
						workFlows.add(contractForm.getWorkFlowList().get(i).getValue());
					}
					// list.add(cb.in(root.<String> get("workFlow"),
					// contractForm.getWorkFlow().getValue()));
					list.add(root.<String> get("workFlow").in(workFlows));
				}

				if (null != contractForm.getCustomerId()) {
					list.add(cb.equal(root.<Customer> get("customer").<Long> get("id"), contractForm.getCustomerId()));
				}

				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		};

	}
	
	private Specification<QuoteContract> buildSpecification(final QuoteContractForm contractForm,final String state) {
		return new Specification<QuoteContract>() {
			@Override
			public Predicate toPredicate(Root<QuoteContract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != contractForm.getWorkFlowList()) {
					List<String> workFlows = new ArrayList<String>();
					for (int i = 0; i < contractForm.getWorkFlowList().size(); i++) {
						workFlows.add(contractForm.getWorkFlowList().get(i).getValue());
					}
					// list.add(cb.in(root.<String> get("workFlow"),
					// contractForm.getWorkFlow().getValue()));
					list.add(root.<String> get("workFlow").in(workFlows));
				}

				if (null != contractForm.getCustomerId()) {
					list.add(cb.equal(root.<Customer> get("customer").<Long> get("id"), contractForm.getCustomerId()));
				}

				if(!StringUtils.isEmpty(state)) {
					list.add(cb.notEqual(root.<String> get("state"),state));
				}
				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		};

	}

	public ExtFileImageVo getExtFileImage(Long contractId, Integer pageNo, Boolean sealSupported) {
		RenewalAgreement file = extFileService.findByQuoteContract(contractId);
		if (null == file) {
			LOGGER.info("挂牌合同{}准备生成协议", contractId);
			extFileService.genExtFile(get(contractId), sealSupported);
		}
		return extendFileImageService.getFileImage(file, pageNo);
	}

	@Autowired
	FastDateFormat fastDateFormat;

	public List<AssetRenewal> queryOriginalContractList(QuoteContractForm contractForm,String state) {
		Page<QuoteContract> page = queryQuoteContractListAndNotInState(contractForm,state);
		List<AssetRenewal> assetRenewalList = new ArrayList<AssetRenewal>();
		if (null != page && page.getTotalElements() > 0) {
			List<QuoteContract> contents = page.getContent();
			for (Iterator<QuoteContract> iterator = contents.iterator(); iterator.hasNext();) {
				QuoteContract quoteContract = iterator.next();
				AssetRenewal assetRenewal = new AssetRenewal();
				if (null != quoteContract.getOriginalContract()) {
					dozerBeanMapper.map(quoteContract.getOriginalContract(), assetRenewal);
					Date endDate = quoteContract.getOriginalContract().getContractEndDate();

					if (!StringUtils.isEmpty(endDate)) {
						assetRenewal.setEndDate(fastDateFormat.format(endDate));
					}
					assetRenewal.setQuoteContractId(quoteContract.getId());
					assetRenewal.setAssetId(quoteContract.getOriginalContract().getId());
					assetRenewal.setSignState(quoteContract.getSignState());
					assetRenewalList.add(assetRenewal);
				} else {
					LOGGER.warn("挂牌合同ID[" + quoteContract.getId() + "]对应原始合同为空");
				}
			}
		}
		return assetRenewalList;
	}

	public AssetRenewal getOne(Long id, Long customerId) {
		AssetRenewal assetRenewal = new AssetRenewal();
		QuoteContract quoteContract = quoteContractRepository.findOne(id);
		if (null == quoteContract) {
			throw new PontusServiceException("资产不存在");
		}
		OriginalContract originalContract = quoteContract.getOriginalContract();
		if (null == originalContract) {
			throw new PontusServiceException("资产不存在");
		}
		dozerBeanMapper.map(originalContract, assetRenewal);
		assetRenewal.setQuoteContractId(quoteContract.getId());
		Date endDate = quoteContract.getOriginalContract().getContractEndDate();
		if (!StringUtils.isEmpty(endDate)) {
			assetRenewal.setEndDate(fastDateFormat.format(endDate));
		}
		assetRenewal.setAssetId(originalContract.getId());
		return assetRenewal;

	}

	/**
	 * 审核发送短信
	 * 
	 * @param type
	 * @param quoteContract
	 * @param phone
	 */
	@Async
	public void auditSendSms(Boolean type, QuoteContract quoteContract) {
		/*
		 * 成功消息：您好，您投资编号******的合同已挂牌预约成功，请进行后续的合同展期操作，如有疑问，欢迎咨询客服 失败消息：您好，您投资编号******的挂牌预约失败 ，失败原因：预约信息不完整，请重新进行合同的挂牌预约或联系客服，客服电话：**********！ 如有疑问，欢迎咨询客服。
		 */
		String code = null;
		if (type) {
			code = "您好，您投资编号" + quoteContract.getContractCode() + "的合同已挂牌预约成功，请进行后续的合同展期操作，如有疑问，欢迎咨询客服。";
		} else {
			code = "您好，您投资编号" + quoteContract.getContractCode() + "的挂牌预约失败，失败原因：" + quoteContract.getAuditRefuseReason() + "，如有疑问，欢迎咨询客服。";
		}
		// String template = map.get(validateCodeService.getTemplateMap().get(Scene.AUDIT)).createSmsTemplate(code);
		LOGGER.info("审核发送短信:{}", code);
		esbHelper.sendSmsRequest(code, quoteContract.getCustomer().getPhone(), false);
	}

	/**
	 * 挂牌合同审核
	 * 
	 * @param auditRefuseReason
	 * @param id
	 * @param status
	 * @return
	 */
	public Result<String> audit(String auditRefuseReason, Long id, String status) {
		Result<String> result = new Result<String>(Result.Type.SUCCESS);
		try {
			QuoteContractWorkFlow workFolw = Enums.QuoteContractWorkFlow.valueOf(status);
			QuoteContract quoteContract = get(id);
			if (status.equals(quoteContract.getSignState())) {
				return result;
			}
			if (Enums.QuoteContractWorkFlow.PASSED.getValue().equals(quoteContract.getWorkFlow())) {
				result.setType(Result.Type.FAILURE);
				result.addMessage("当前合同已经审核通过，不可再更改状态。");
				return result;
			}
			if (null != auditRefuseReason && workFolw.equals(Enums.QuoteContractWorkFlow.REJECT)) {
				quoteContract.setAuditRefuseReason(auditRefuseReason);
			} else if (workFolw.equals(Enums.QuoteContractWorkFlow.PASSED)) {
				// 查找匹配原始合同
				OriginalContract originalContract = contractService.findByContractCodeAndPlatformAndCertiNo(quoteContract.getContractCode(), quoteContract.getPlatform(), quoteContract.getCustomer().getIdCardAccount());
				if (null == originalContract) {
					result.setType(Result.Type.FAILURE);
					result.addMessage("无法找到匹配原始合同...");
					return result;
				}
				if (OriginalContractWorkFlow.BOUND.getValue().equals(originalContract.getWorkFlow())) {
					result.setType(Result.Type.FAILURE);
					result.addMessage("匹配原始合同,已经被绑定...");
					return result;
				} else {
					quoteContract.setOriginalContract(originalContract);
					originalContract.setWorkFlow(OriginalContractWorkFlow.BOUND.getValue());
					contractService.saveOrUpdate(originalContract);
				}
			}
			quoteContract.setWorkFlow(workFolw.getValue());
			if (workFolw.equals(Enums.QuoteContractWorkFlow.PASSED)) {
				auditSendSms(true, quoteContract);
			}
			if (workFolw.equals(Enums.QuoteContractWorkFlow.REJECT)) {
				auditSendSms(false, quoteContract);
			}
			saveOrUpdate(quoteContract);
		} catch (Exception e) {
			result.setType(Result.Type.FAILURE);
			LOGGER.error("审核异常", e);
		}
		return result;
	}

	public Long countAll(Long customerId) {
		return quoteContractRepository.countAll(customerId);
	}

	public Long countByCustomerandWorkFlow(Long customerId, String flow) {
		return quoteContractRepository.countByCustomerandWorkFlow(customerId, flow);
	}

}
