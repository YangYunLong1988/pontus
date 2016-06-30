package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.Platform;

@Repository
public interface PlatformRepository extends PagingAndSortingRepository<Platform, Long>, JpaSpecificationExecutor<Platform> {

	@Query("from Platform as a left join a.adminUserSet as b where b.id=?1")
	public List<Platform> getAdminUserPlatform(Long adminUserId);
}
