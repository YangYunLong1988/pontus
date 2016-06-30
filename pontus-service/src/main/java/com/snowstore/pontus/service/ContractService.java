package com.snowstore.pontus.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.repository.OriginalContractRepository;
import com.snowstore.pontus.repository.QuoteContractRepository;
import com.snowstore.pontus.service.vo.ContractQueryForm;
import com.snowstore.pontus.service.vo.Result;

@Service
@Transactional
public class ContractService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContractService.class);

	@Autowired
	private OriginalContractRepository originalContractRepository;

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private QuoteContractRepository quoteContractRepository;

	public Page<OriginalContract> findAll(Specification<OriginalContract> spc, Pageable pageable, ContractQueryForm contractQueryForm) {
		Page<OriginalContract> page = originalContractRepository.findAll(buildSpecification(contractQueryForm), pageable);
		LOGGER.info("原始合同查询,共{}条", page.getTotalElements());
		return page;
	}

	public Specification<OriginalContract> buildSpecification(final ContractQueryForm contractQueryForm) {
		return new Specification<OriginalContract>() {
			@Override
			public Predicate toPredicate(Root<OriginalContract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				if (null != contractQueryForm.getStatus()) {
					list.add(cb.equal(root.<String> get("status"), contractQueryForm.getStatus()));
				}

				List<String> platforms = adminUserService.getUserPlatforms();
				if (null != contractQueryForm.getProductResource()) {
					list.add(cb.equal(root.<String> get("platform"), contractQueryForm.getProductResource()));
				} else if (!platforms.isEmpty()) {
					list.add(root.<String> get("platform").in(platforms));
				}

				if (null != contractQueryForm.getQuery()) {
					list.add(cb.or(cb.like(root.<String> get("contractCode"), '%' + contractQueryForm.getQuery() + '%'), cb.like(root.<String> get("productName"), '%' + contractQueryForm.getQuery() + '%'), cb.like(root.<String> get("certiNo"), '%' + contractQueryForm.getQuery() + '%'), cb.like(root.<String> get("investorName"), '%' + contractQueryForm.getQuery() + '%')));
				}

				return cb.and(list.toArray(new Predicate[list.size()]));
			}

		};

	}

	public OriginalContract get(Long id) {
		return originalContractRepository.findOne(id);
	}

	/**
	 * 根据购买平台和合同号查询原始合同
	 * 
	 * @param contractCode
	 * @param platform
	 * @return
	 */
	public OriginalContract findByContractCodeAndPlatformAndCertiNo(String contractCode, String platform, String certiNo) {
		return originalContractRepository.findByContractCodeAndPlatformAndCertiNo(contractCode, platform, certiNo);
	}

	/**
	 * 根据合同编号号查询原始合同
	 * 
	 * @param contractCode
	 * @return
	 */
	public OriginalContract findByContractCode(String contractCode) {
		return originalContractRepository.findByContractCode(contractCode);
	}

	public OriginalContract saveOrUpdate(OriginalContract originalContract) {
		return originalContractRepository.save(originalContract);
	}

	/**
	 * 根据合同编号和状态取反查询合同
	 * 
	 * @date 2016年6月21日
	 * @param contractCode
	 * @param status
	 * @return
	 */
	public Result<OriginalContract> findByContractCodeAndStatusNot(String contractCode, String status) {
		Result<OriginalContract> result = new Result<OriginalContract>(Result.Type.FAILURE);
		OriginalContract oc = originalContractRepository.findByContractCodeAndStatus(contractCode, status);
		if (null != oc) {
			QuoteContract quoteContract = quoteContractRepository.findByContractCode(oc.getContractCode());
			if (null != quoteContract) {
				if (QuoteContractWorkFlow.SUCCESS.getValue().equals(quoteContract.getWorkFlow())) {// 转让成功无法添加特殊兑付
					result.addMessage("挂牌成功,无法添加特殊兑付！");
					return result;
				}
				if (QuoteContractWorkFlow.TRANSFERING.getValue().equals(quoteContract.getWorkFlow())) {
					result.addMessage("警告:此合同转让单状态转让中！");
					result.setType(Result.Type.WARNING);
					result.setData(oc);
					return result;
				}
			}
			result.setData(oc);
			result.setType(Result.Type.SUCCESS);
		}else {
			result.addMessage("未找到对应的合同!");
		}
		return result;
	}
}
