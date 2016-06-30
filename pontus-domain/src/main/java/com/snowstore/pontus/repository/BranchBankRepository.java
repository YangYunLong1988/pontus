package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.BranchBank;

@Repository
public interface BranchBankRepository extends PagingAndSortingRepository<BranchBank, Long>, JpaSpecificationExecutor<BranchBank> {
	@Query("from BranchBank a  where a.city = ?1 and a.bankName = ?2 and a.status = ?3")
	List<BranchBank> findBranchBanks(String city, String bankName, String status);
	
	@Query("from BranchBank a where a.province =?1 and a.city = ?2 and a.bankName = ?3 and a.status = ?4")
	List<BranchBank> findBranchBanks(String province,String city, String bankName, String status);

	@Query("from BranchBank a  where a.bankName = ?1 and a.branchBankName = ?2 and a.status = ?3")
	BranchBank findBranchBanksByBankNameAndBranchBankName(String bankName, String branchBankName, String status);
}
