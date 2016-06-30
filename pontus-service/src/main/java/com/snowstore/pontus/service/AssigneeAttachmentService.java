package com.snowstore.pontus.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.snowstore.pontus.domain.Assignee;
import com.snowstore.pontus.domain.AssigneeAttachment;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.AssigneeWorkAttachFlow;
import com.snowstore.pontus.repository.AssigneeAttachmentRepository;
import com.snowstore.pontus.repository.AssigneeRepository;

@Service
@Transactional
public class AssigneeAttachmentService {
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private AssigneeAttachmentRepository assigneeAttachmentRepository;
	@Autowired
	private AssigneeRepository assigneeRepository;
	@Autowired
	private GridfsService gridfsService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AssigneeAttachmentService.class);

	public void saveAssigneeAttachment(byte[] bytes, String fileName, String customerId, Long assigneeId, String attachType) {
		GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), fileName);
		Assignee assignee = assigneeRepository.findOne(assigneeId);
		AssigneeAttachment assigneeAttachment = new AssigneeAttachment();
		assigneeAttachment.setAssignee(assignee);
		assigneeAttachment.setName(fileName);
		assigneeAttachment.setAttachType(attachType);
		assigneeAttachment.setObjectId(gridFile.getId().toString());
		assigneeAttachment.setState(Enums.AssigneeAttachmentState.VALID.getValue());

		assignee.setAttachFlow(AssigneeWorkAttachFlow.PENDING.getValue());
		assigneeRepository.save(assignee);
		assigneeAttachmentRepository.save(assigneeAttachment);
		LOGGER.debug("保存附件，受让id【" + assigneeId + "】，附件类型【" + attachType + "】成功");
	}

	public AssigneeAttachment saveAssigneeAttachment(byte[] bytes, String fileName, String attachType) {
		GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), fileName);
		AssigneeAttachment assigneeAttachment = new AssigneeAttachment();
		assigneeAttachment.setName(fileName);
		assigneeAttachment.setAttachType(attachType);
		assigneeAttachment.setObjectId(gridFile.getId().toString());
		assigneeAttachment.setState(Enums.AssigneeAttachmentState.VALID.getValue());
		return assigneeAttachmentRepository.save(assigneeAttachment);
	}

	public List<String> getAssigneeAttachment(Long customerId, Long assigneeId, String attachType) {

		List<String> ret = new ArrayList<String>();
		List<AssigneeAttachment> assigneeAttachmentList = assigneeAttachmentRepository.findByAssigneeIdAndAttachType(assigneeId, attachType);
		try {
			if (!CollectionUtils.isEmpty(assigneeAttachmentList)) {
				for (Iterator<AssigneeAttachment> iterator = assigneeAttachmentList.iterator(); iterator.hasNext();) {
					AssigneeAttachment assigneeAttachment = iterator.next();
					ret.add(gridfsService.getBase64Content(assigneeAttachment.getObjectId()));
				}
			}
		} catch (IOException e) {
			LOGGER.error("读取受让附件异常【" + e + "】");
			throw new PontusServiceException("读取受让附件异常");
		}
		return ret;
	}

	public byte[] getContent(AssigneeAttachment attachment) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GridFSDBFile fsdb = getGridFSDBFile(attachment);
		if (fsdb != null) {
			try {
				fsdb.writeTo(out);
			} catch (IOException e) {
				throw new PontusServiceException();
			}
		}
		return out.toByteArray();
	}

	public byte[] getContent(Long attachmentId) {
		return this.getContent(this.get(attachmentId));
	}

	private GridFSDBFile getGridFSDBFile(AssigneeAttachment attachment) {
		if (attachment == null || attachment.getObjectId() == null) {
			return null;
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(attachment.getObjectId())));
		return gridFsTemplate.findOne(query);
	}

	public AssigneeAttachment get(Long id) {
		return assigneeAttachmentRepository.findOne(id);
	}

	public List<AssigneeAttachment> getByAssigneeAndAttachType(Long assignee, String attachType,String state) {
		return assigneeAttachmentRepository.findByAssigneeAndAttachTypeAndState(assigneeRepository.findOne(assignee), attachType,state);
	}

	public List<AssigneeAttachment> getByAssigneeId(Long id) {
		LOGGER.info("根据受让单获取协议");
		return assigneeAttachmentRepository.findByAssignee(assigneeRepository.findOne(id));
	}

	public List<AssigneeAttachment> getAssigneeAttachment(final Long assignee, final String attachType) {
		return assigneeAttachmentRepository.findAll(new Specification<AssigneeAttachment>() {
			@Override
			public Predicate toPredicate(Root<AssigneeAttachment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("assignee"), assignee), cb.equal(root.get("attachType"), attachType));
			}
		});
	}

	public void bind(Long assigneeId, List<Long> attacIds) {
		for (AssigneeAttachment attac : getByAssigneeId(assigneeId)) {
			if (attacIds.contains(attac.getId())) {
				attacIds.remove(attac.getId());
			} else {
				assigneeAttachmentRepository.delete(attac);
			}
		}
		Assignee assignee = assigneeRepository.findOne(assigneeId);
		assignee.setAttachFlow(AssigneeWorkAttachFlow.PENDING.getValue());
		assigneeRepository.save(assignee);
		// 绑定附件
		for (Long attacId : attacIds) {
			if (attacId != null) {
				AssigneeAttachment attac = get(attacId);
				attac.setAssignee(assignee);
				assigneeAttachmentRepository.save(attac);
			}
		}
	}

	public List<AssigneeAttachment> getObjectIdByAssigneeId(Long id) {
		if (id == null) {
			return null;
		}
		return assigneeAttachmentRepository.findByAssigneeId(id);
	}

	public AssigneeAttachment getQuoteContractAttachmentByObjectId(final String objId) {
		return assigneeAttachmentRepository.findOne(new Specification<AssigneeAttachment>() {
			@Override
			public Predicate toPredicate(Root<AssigneeAttachment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("objectId"), objId);
			}
		});
	}
	
	public void save (AssigneeAttachment assigneeAttachment) {
		this.assigneeAttachmentRepository.save(assigneeAttachment);
	}

}
