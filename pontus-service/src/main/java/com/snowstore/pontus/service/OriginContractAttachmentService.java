package com.snowstore.pontus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.OriginalContractAttachment;
import com.snowstore.pontus.repository.OriginalContractAttachmentRepository;

@Service
@Transactional
public class OriginContractAttachmentService {
	@Autowired
	private OriginalContractAttachmentRepository originalContractAttachmentRepository;

	public List<OriginalContractAttachment> loadQuoteContractAttachment(OriginalContract originalContract) {
		return originalContractAttachmentRepository.findByOriginalContract(originalContract);
	}

}
