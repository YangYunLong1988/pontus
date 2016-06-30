package com.snowstore.pontus.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.BankInfo;

@Repository
public interface BankInfoRepository extends PagingAndSortingRepository<BankInfo, Long>, JpaSpecificationExecutor<BankInfo> {

	@Query("from BankInfo a where a.customer.id =?1 and a.defaulted=1")
	BankInfo findByCustomerId(Long customerId);

	@Query("select count(a.id) from BankInfo a where a.customer.id =?1")
	int countByCustomerId(Long customerId);

	@Query("from BankInfo a where a.name =?1 and a.account = ?2")
	BankInfo findByNameAndAccount(String name, String account);
}
