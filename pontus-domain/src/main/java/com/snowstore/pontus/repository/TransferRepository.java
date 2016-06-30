package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.Transfer;

@Repository
public interface TransferRepository extends PagingAndSortingRepository<Transfer, Long>, JpaSpecificationExecutor<Transfer> {

	@Query("from Transfer a where a.quoteContract.id = ?1 and a.state =?2")
	public List<Transfer> queryByQuoteContractIdAndState(Long quoteContractId, String state);

	@Query("select count(a.id) from Transfer a where a.quoteContract.customer.id = ?1 and a.state='有效'")
	public Long countTransferRecord(Long userId);
}
