package com.snowstore.pontus.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.MemberAttachment;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.repository.CustomerRepository;
import com.snowstore.pontus.repository.MemberAttachmentRepository;

@Service
@Transactional
public class MemberAttachmentService {
	@Autowired
	private MemberAttachmentRepository memberAttachmentRepository;
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private GridfsService gridfsService;
	@Autowired
	private CustomerService customerService;

	private static final Logger LOGGER = LoggerFactory.getLogger(MemberAttachmentService.class);

	public MemberAttachment get(Long id) {
		return memberAttachmentRepository.findOne(id);
	}

	public void save(byte[] bytes, String fileName, String fileContentType) {
		GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), fileName, fileContentType);
		MemberAttachment attachment = new MemberAttachment();
		attachment.setName(fileName);
		attachment.setStatus("");
		attachment.setObjectId(gridFile.getId().toString());
		memberAttachmentRepository.save(attachment);
	}

	public MemberAttachment bind(Long id, Customer customer) {
		MemberAttachment memberAttachment = get(id);
		memberAttachment.setCustomer(customer);
		return memberAttachment;
	}

	public void saveNameCard(byte[] bytes, String fileName, String customerId) {
		GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), fileName);
		Customer customer = customerRepository.findOne(Long.valueOf(customerId));
		MemberAttachment attachment = null;
		attachment = memberAttachmentRepository.findByCustomerIdAndAttachType(Long.valueOf(customerId), Enums.AttachType.CARD_PERSON.getValue());
		if (null == attachment) {
			attachment = new MemberAttachment();
			attachment.setCustomer(customer);
			attachment.setAttachType(Enums.AttachType.CARD_PERSON.getValue());
			attachment.setName(fileName);
			attachment.setStatus("");
		} else {
			LOGGER.debug("更新用户名片附件表");
		}
		attachment.setObjectId(gridFile.getId().toString());

		memberAttachmentRepository.save(attachment);
		LOGGER.debug("保存用户【" + customerId + "】名片成功");
	}

	public List<MemberAttachment> loadMemberAttachment(Customer customer) {
		return memberAttachmentRepository.findByCustomer(customer);
	}

	/**
	 * 返回 base64
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月9日 下午1:32:14
	 * @param bytes
	 * @param fileName
	 * @param customerId
	 * @return
	 */
	public List<String> getNameCard(Long customerId, String attachType) {
		List<String> ret = new ArrayList<String>();
		MemberAttachment memberAttachment = memberAttachmentRepository.findByCustomerIdAndAttachType(customerId, attachType);
		if (null == memberAttachment) {
			LOGGER.error("会员附件表为空");
			throw new PontusServiceException("文件不存在");
		}
		if (StringUtils.isEmpty(memberAttachment.getObjectId())) {
			LOGGER.error("id为[" + memberAttachment.getId() + "]会员附件表中的objectid为空");
			throw new PontusServiceException("文件不存在");
		}
		try {
			ret.add(gridfsService.getBase64Content(memberAttachment.getObjectId()));
		} catch (IOException e) {
			LOGGER.error("读取附件异常【" + e + "】");
			throw new PontusServiceException("读取附件异常");
		}
		return ret;
	}

	public MemberAttachment findOne(Long id) {
		return memberAttachmentRepository.findOne(id);
	}

	public MemberAttachment saveMemberAttachment(byte[] bytes, String originalFilename, String type) {
		GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), originalFilename, type);
		MemberAttachment memberAttachment = new MemberAttachment();
		memberAttachment.setAttachType(type);
		memberAttachment.setName(originalFilename);
		memberAttachment.setObjectId(gridFile.getId().toString());
		return memberAttachmentRepository.save(memberAttachment);
	}

	public byte[] getContent(Long attachmentId) {
		return this.getContent(this.get(attachmentId));
	}

	public byte[] getContent(MemberAttachment memberAttachment) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GridFSDBFile fsdb = getGridFSDBFile(memberAttachment);
		if (fsdb != null) {
			try {
				fsdb.writeTo(out);
			} catch (IOException e) {
				throw new PontusServiceException();
			}
		}
		return out.toByteArray();
	}

	private GridFSDBFile getGridFSDBFile(MemberAttachment memberAttachment) {
		if (memberAttachment == null || memberAttachment.getObjectId() == null) {
			return null;
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(memberAttachment.getObjectId())));
		return gridFsTemplate.findOne(query);
	}

	public void bind(Long imgId, Long userId, String type) {
		try {
			MemberAttachment memberAttachment = get(imgId);
			Customer customer = customerService.get(userId);

			memberAttachment.setCustomer(customer);
			memberAttachment.setAttachType(type);
			memberAttachmentRepository.save(memberAttachment);
		} catch (Exception e) {
			return;
		}

	}

	public Long findImgIdByUserIdAndWorkFlow(Long userId, String workFlow) {
		List<MemberAttachment> mas = memberAttachmentRepository.findByCustomerIdAndAttachType2(userId, workFlow);
		if (mas != null && mas.size() > 0) {
			return mas.get(0).getId();
		} else {
			return null;
		}
	}
}
