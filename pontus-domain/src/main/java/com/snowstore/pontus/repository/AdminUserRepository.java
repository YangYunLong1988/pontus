package com.snowstore.pontus.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.AdminUser;

@Repository
public interface AdminUserRepository extends PagingAndSortingRepository<AdminUser, Long>, JpaSpecificationExecutor<AdminUser> {

	public AdminUser findByUsername(String username);
}
