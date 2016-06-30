package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.Assignee;

@Repository
public interface AssigneeRepository extends PagingAndSortingRepository<Assignee, Long>, JpaSpecificationExecutor<Assignee> {

	@Query("from Assignee s where s.transfer.id=?1")
	public List<Assignee> getListAssignee(Long transferId);

	@Query("from Assignee s where s.transfer.id=?2 and s.state = ?3 and s.customer.id = ?1")
	public List<Assignee> findByCustomerIdAndState(Long customerId, Long transferId, String state);

	@Query("from Assignee s where s.transfer.id=?2 and s.workFlow <> ?3 and s.customer.id = ?1")
	public List<Assignee> findByCustomerIdAndNotInWorkFlow(Long customerId, Long transferId, String workFlow);
	
	@Query("from Assignee s where s.workFlow <> ?2 and s.customer.id = ?1 and s.bankInfo.id = ?3")
	public List<Assignee> findByCustomerIdAndBankIdAndNotInWorkFlow(Long customerId, String workFlow,Long bankId);

	@Query("from Assignee s where s.transfer.id=?1 and s.customer.id = ?2")
	public List<Assignee> findByTransferAndCustommer(Long id, Long customerId);
	
	@Query("from Assignee s where s.customer.id = ?1 and s.workFlow = ?2")
	public List<Assignee> findByCustommerAndWorkFlow(Long customerId,String workFlow);

	@Query("select count(s.id) from Assignee s where s.customer.id = ?1")
	public Long countTransfeeRecord(Long userId);
}
