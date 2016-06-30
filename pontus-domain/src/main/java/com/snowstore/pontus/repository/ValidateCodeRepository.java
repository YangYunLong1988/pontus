package com.snowstore.pontus.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.ValidateCode;

@Repository
public interface ValidateCodeRepository extends PagingAndSortingRepository<ValidateCode, Long>, JpaSpecificationExecutor<ValidateCode> {

}
