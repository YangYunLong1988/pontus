package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.MemberAttachment;

@Repository
public interface MemberAttachmentRepository extends PagingAndSortingRepository<MemberAttachment, Long>, JpaSpecificationExecutor<MemberAttachment> {

	public List<MemberAttachment> findByCustomer(Customer customer);

	@Query("from MemberAttachment a where a.customer.id =?1 and a.attachType = ?2")
	MemberAttachment findByCustomerIdAndAttachType(Long customerId, String attachType);

	@Query("from MemberAttachment a where a.customer.id =?1 and a.attachType = ?2 order by a.lastModifiedDate desc")
	List<MemberAttachment> findByCustomerIdAndAttachType2(Long customerId, String attachType);
}
