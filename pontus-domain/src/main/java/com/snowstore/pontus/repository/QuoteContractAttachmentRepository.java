package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.domain.QuoteContractAttachment;

@Repository
public interface QuoteContractAttachmentRepository extends PagingAndSortingRepository<QuoteContractAttachment, Long>, JpaSpecificationExecutor<QuoteContractAttachment> {

	@Query("from QuoteContractAttachment a where a.quoteContract.id =?1 and a.attachType=?2")
	List<QuoteContractAttachment> findQuoteContract(Long quoteContractId, String attachType);

	@Query("from QuoteContractAttachment a where a.quoteContract.id =?1")
	List<QuoteContractAttachment> findQuoteContract(Long quoteContractId);

	public List<QuoteContractAttachment> findByQuoteContract(QuoteContract quoteContract);
}
