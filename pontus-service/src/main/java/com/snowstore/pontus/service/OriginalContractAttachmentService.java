package com.snowstore.pontus.service;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.gridfs.GridFSFile;
import com.snowstore.pontus.domain.OriginalContractAttachment;
import com.snowstore.pontus.repository.OriginalContractAttachmentRepository;

@Service
@Transactional
public class OriginalContractAttachmentService {
	@Autowired
	private OriginalContractAttachmentRepository originalContractAttachmentRepository;
	@Autowired
	private GridFsTemplate gridFsTemplate;

	public OriginalContractAttachment get(Long id) {
		return originalContractAttachmentRepository.findOne(id);
	}

	public void save(byte[] bytes, String fileName, String fileContentType) {
		GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), fileName, fileContentType);
		OriginalContractAttachment attachment = new OriginalContractAttachment();
		attachment.setName(fileName);
		attachment.setStatus("");
		attachment.setObjectId(gridFile.getId().toString());
		originalContractAttachmentRepository.save(attachment);
	}
}
