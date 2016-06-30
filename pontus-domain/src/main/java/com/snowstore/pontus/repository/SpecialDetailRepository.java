package com.snowstore.pontus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.SpecialContract;
import com.snowstore.pontus.domain.SpecialDetail;

@Repository
public interface SpecialDetailRepository extends PagingAndSortingRepository<SpecialDetail, Long>, JpaSpecificationExecutor<SpecialDetail> {

	Page<SpecialDetail> findBySpecialContract(SpecialContract specialContract, Pageable pageable);

	List<SpecialDetail> findBySpecialContractId(Long id);

}
