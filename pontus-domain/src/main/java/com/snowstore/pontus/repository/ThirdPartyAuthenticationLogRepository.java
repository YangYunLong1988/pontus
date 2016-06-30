package com.snowstore.pontus.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.ThirdPartyAuthenticationLog;

@Repository
public interface ThirdPartyAuthenticationLogRepository extends PagingAndSortingRepository<ThirdPartyAuthenticationLog, Long>, JpaSpecificationExecutor<ThirdPartyAuthenticationLog> {

}
