package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.Assignee;
import com.snowstore.pontus.domain.AssigneeAttachment;

@Repository
public interface AssigneeAttachmentRepository extends PagingAndSortingRepository<AssigneeAttachment, Long>, JpaSpecificationExecutor<AssigneeAttachment> {

	public List<AssigneeAttachment> findByAssigneeAndAttachTypeAndState(Assignee assignee, String attachType,String state);

	@Query("from AssigneeAttachment s where s.assignee.id=?1 and s.attachType = ?2")
	public List<AssigneeAttachment> findByAssigneeIdAndAttachType(Long assigneeId, String attachType);

	public List<AssigneeAttachment> findByAssignee(Assignee assignee);

	@Query("from AssigneeAttachment s where s.assignee.id=?1")
	public List<AssigneeAttachment> findByAssigneeId(Long id);
}
