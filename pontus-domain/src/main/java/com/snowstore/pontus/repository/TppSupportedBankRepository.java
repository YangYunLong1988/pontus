package com.snowstore.pontus.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.TppSupportedBank;

@Repository
public interface TppSupportedBankRepository extends PagingAndSortingRepository<TppSupportedBank, Long>, JpaSpecificationExecutor<TppSupportedBank> {

}
