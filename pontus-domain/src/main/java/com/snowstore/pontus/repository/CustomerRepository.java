package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.Customer;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

	public Customer findByEmail(String email);

	public Customer findByUsername(String username);

	public Customer findByPhone(String phone);

	public Customer findByIdCardNameAndIdCardAccountAndWorkFlow(String idCardName, String idCardAccount, String workFlow);

	public Customer findByIdCardAccountAndWorkFlow(String idCardAccount, String workFlow);

	@Query(" from Customer a where a.idCardAccount=?1")
	public List<Customer> findByIdCard(String idcard);
}
