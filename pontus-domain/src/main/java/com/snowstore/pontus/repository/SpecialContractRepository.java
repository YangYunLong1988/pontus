package com.snowstore.pontus.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.SpecialContract;

@Repository
public interface SpecialContractRepository extends PagingAndSortingRepository<SpecialContract, Long>, JpaSpecificationExecutor<SpecialContract> {

	SpecialContract findByOriginalContract(OriginalContract originalContract);

}
