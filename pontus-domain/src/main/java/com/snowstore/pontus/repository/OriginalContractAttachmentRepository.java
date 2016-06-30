package com.snowstore.pontus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.OriginalContractAttachment;

@Repository
public interface OriginalContractAttachmentRepository extends PagingAndSortingRepository<OriginalContractAttachment, Long>, JpaSpecificationExecutor<OriginalContractAttachment> {
	public List<OriginalContractAttachment> findByOriginalContract(OriginalContract originalContract);
}
