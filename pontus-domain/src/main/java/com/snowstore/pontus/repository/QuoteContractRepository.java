package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.QuoteContract;

@Repository
public interface QuoteContractRepository extends PagingAndSortingRepository<QuoteContract, Long>, JpaSpecificationExecutor<QuoteContract> {

	public QuoteContract findByContractCodeAndCustomerId(String contractCode, Long customerId);

	@Query("from QuoteContract a where a.contractCode = ?1 and a.customer.id = ?2 and a.workFlow in(?3)")
	public List<QuoteContract> findByContractCodeAndCustomerIdAndWorkFlows(String contractCode, Long customerId, List<String> workFLows);

	@Query("from QuoteContract a where a.contractCode = ?1 and a.workFlow in(?2) and a.state='有效'")
	public List<QuoteContract> findByContractCodeAndWorkFlows(String contractCode, List<String> workFLows);

	public QuoteContract findByIdAndCustomerId(Long id, Long customerId);

	@Modifying
	@Query("update QuoteContract c set c.workFlow = ?2 where c.id = ?1")
	public int updateWorkFlow(Long id, String workFlow);

	@Query("select count(a.id) from QuoteContract a where a.customer.id = ?1 and a.workFlow !='新建' and a.state='有效' ")
	public Long countAll(Long customerId);

	@Query("select count(a.id) from QuoteContract a where a.customer.id = ?1 and a.workFlow=?2 and a.state='有效'")
	public Long countByCustomerandWorkFlow(Long customerId, String flow);

	public QuoteContract findByOriginalContract(OriginalContract originalContract);

	public QuoteContract findByContractCode(String contractCode);

	@Query("select a.originalContract.id from QuoteContract a where a.customer.id = ?1")
	public List<Long> findByCustomerId(Long customerId);

}
