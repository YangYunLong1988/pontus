package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.QuoteContract;

@Repository
public interface OriginalContractRepository extends PagingAndSortingRepository<OriginalContract, Long>, JpaSpecificationExecutor<OriginalContract> {
	public OriginalContract findByContractCodeAndPlatformAndCertiNo(String contractCode, String platform, String certiNo);

	public OriginalContract findByContractCode(String contractCode);

	public OriginalContract findByQuoteContract(QuoteContract quoteContract);

	public List<OriginalContract> findByStatus(String status);

	public OriginalContract findByContractCodeAndStatus(String contractCode, String status);

}
