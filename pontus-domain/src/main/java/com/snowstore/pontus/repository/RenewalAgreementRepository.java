package com.snowstore.pontus.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.RenewalAgreement;

@Repository
public interface RenewalAgreementRepository extends PagingAndSortingRepository<RenewalAgreement, Long>, JpaSpecificationExecutor<RenewalAgreement> {

	@Query("from RenewalAgreement a where a.quoteContract.id =?1")
	RenewalAgreement findQuoteContract(Long quoteContractId);
}
