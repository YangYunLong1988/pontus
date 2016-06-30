package com.snowstore.pontus.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
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
import org.springframework.util.StringUtils;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.domain.QuoteContractAttachment;
import com.snowstore.pontus.domain.RenewalAgreement;
import com.snowstore.pontus.repository.QuoteContractAttachmentRepository;
import com.snowstore.pontus.repository.QuoteContractRepository;
import com.snowstore.pontus.repository.RenewalAgreementRepository;
import com.snowstore.pontus.service.pdf.PdfFactory;
import com.snowstore.pontus.service.vo.ExtFileImageVo;

@Service
@Transactional
public class QuoteContractAttachmentService {
	@Autowired
	private QuoteContractAttachmentRepository quoteContractAttachmentRepository;
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private PdfFactory pdfFactory;
	@Autowired
	private QuoteContractRepository quoteContractRepository;
	@Autowired
	private RenewalAgreementRepository renewalAgreementRepository;
	@Autowired
	private QuoteContractService quoteContractService;
	@Autowired
	private GridfsService gridfsService;

	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteContractAttachmentService.class);

	public void saveQuoteContractAttachment(byte[] bytes, String fileName, String customerId, Long quoteContractId, String attachType) {
		GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), fileName);
		// Customer customer =
		// customerRepository.findOne(Long.valueOf(customerId));
		QuoteContractAttachment quoteContractAttachment = new QuoteContractAttachment();
		quoteContractAttachment.setAttachType(attachType);
		quoteContractAttachment.setName(fileName);
		quoteContractAttachment.setObjectId(gridFile.getId().toString());
		quoteContractAttachment.setQuoteContract(quoteContractRepository.findOne(quoteContractId));
		quoteContractAttachmentRepository.save(quoteContractAttachment);
		LOGGER.debug("保存附件，合同id【" + quoteContractId + "】，附件类型【" + attachType + "】成功");
	}

	public QuoteContractAttachment saveQuoteContractAttachment(byte[] bytes, String fileName, String attachType) {
		GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), fileName);
		QuoteContractAttachment quoteContractAttachment = new QuoteContractAttachment();
		quoteContractAttachment.setAttachType(attachType);
		quoteContractAttachment.setName(fileName);
		quoteContractAttachment.setObjectId(gridFile.getId().toString());
		return quoteContractAttachmentRepository.save(quoteContractAttachment);
	}

	public byte[] getContent(QuoteContractAttachment attachment) {
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

	private GridFSDBFile getGridFSDBFile(QuoteContractAttachment attachment) {
		if (attachment == null || attachment.getObjectId() == null) {
			return null;
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(attachment.getObjectId())));
		return gridFsTemplate.findOne(query);
	}

	public QuoteContractAttachment getQuoteContractAttachmentByObjectId(final String objectId) {
		return quoteContractAttachmentRepository.findOne(new Specification<QuoteContractAttachment>() {
			@Override
			public Predicate toPredicate(Root<QuoteContractAttachment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("objectId"), objectId);
			}
		});
	}
	
	public byte[] getContentByObjectId(String objId) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		QuoteContractAttachment attachment = new QuoteContractAttachment();
		attachment.setObjectId(objId);
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

	public void bind(Long id, Long quoteContractId) {
		QuoteContractAttachment quoteContractAttachment = get(id);
		quoteContractAttachment.setQuoteContract(quoteContractRepository.findOne(quoteContractId));
		quoteContractAttachmentRepository.save(quoteContractAttachment);
	}

	public QuoteContractAttachment get(Long id) {
		return quoteContractAttachmentRepository.findOne(id);
	}

	public List<String> getQuoteContractAttachment(Long customerId, Long quoteContractId, String attachType) {

		List<String> ret = new ArrayList<String>();
		List<QuoteContractAttachment> quoteContractAttachmentList = quoteContractAttachmentRepository.findQuoteContract(quoteContractId, attachType);
		try {
			if (!CollectionUtils.isEmpty(quoteContractAttachmentList)) {
				for (Iterator<QuoteContractAttachment> iterator = quoteContractAttachmentList.iterator(); iterator.hasNext();) {
					QuoteContractAttachment quoteContractAttachment2 = iterator.next();
					ret.add(gridfsService.getBase64Content(quoteContractAttachment2.getObjectId()));
				}
			}
		} catch (IOException e) {
			LOGGER.error("读取附件异常【" + e + "】");
			throw new PontusServiceException("读取附件异常");
		}
		return ret;
	}

	public List<byte[]> genPdfImage(byte[] pdfFile) {
		List<byte[]> images = new ArrayList<byte[]>();
		ByteArrayOutputStream out;
		BufferedImage image;
		try {
			PDDocument doc = PDDocument.load(new ByteArrayInputStream(pdfFile));
			@SuppressWarnings("unchecked")
			List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
			for (int i = 0; i < pages.size(); i++) {
				out = new ByteArrayOutputStream();
				image = pages.get(i).convertToImage();
				ImageIO.write(image, "png", out);
				images.add(out.toByteArray());
			}
		} catch (IOException e) {
		}
		return images;
	}

	/**
	 * 展期协议预览
	 * 
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月7日 下午3:45:21
	 * @param quoteContractId
	 */
	public ExtFileImageVo getProtocol(Long quoteContractId, int currentPage) {
		ExtFileImageVo imageVo = null;

		QuoteContract quoteContract = quoteContractRepository.findOne(quoteContractId);
		RenewalAgreement renewalAgreement = renewalAgreementRepository.findQuoteContract(quoteContractId);
		// 查询有没有生成展期协议，没有就生成，有就转换返回图片
		if (null != renewalAgreement) {
			if (!StringUtils.isEmpty(renewalAgreement.getObjectId())) {
				try {
					// 转换为图片
					imageVo = quoteContractService.getExtFileImage(quoteContractId, currentPage, false);
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
					throw new PontusServiceException("图片转换异常", e);
				}
			} else {
				LOGGER.debug("根据挂牌合同id[" + quoteContractId + "]查询展期协议表有数据，但objectid为空");
			}
		} else {
			LOGGER.debug("根据挂牌合同id[" + quoteContractId + "]查询展期协议表无数据.开始生成展期协议");
			// 生成协议
			byte[] bytes = pdfFactory.buildKLSupplementaryFile(quoteContract, false);
			// 保存monmgoDB
			GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), "", "application/pdf");
			// 生成展期协议表
			RenewalAgreement agreement = new RenewalAgreement();
			agreement.setQuoteContract(quoteContract);
			agreement.setObjectId(gridFile.getId().toString());
			renewalAgreementRepository.save(agreement);

			// 转换为图片
			imageVo = quoteContractService.getExtFileImage(quoteContractId, currentPage, false);

		}

		return imageVo;
	}

	public List<QuoteContractAttachment> loadQuoteContractAttachment(QuoteContract quoteContract) {
		return quoteContractAttachmentRepository.findByQuoteContract(quoteContract);
	}

	public List<QuoteContractAttachment> queryQuoteContractAttachment(final Long quoteContractId, final String attachType) {
		return quoteContractAttachmentRepository.findAll(new Specification<QuoteContractAttachment>() {
			@Override
			public Predicate toPredicate(Root<QuoteContractAttachment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.equal(root.get("quoteContract"), quoteContractId), cb.equal(root.get("attachType"), attachType));
			}
		});
	}

	public void delete(QuoteContractAttachment quoteContractAttachment) {
		quoteContractAttachmentRepository.delete(quoteContractAttachment);
	}

	public List<QuoteContractAttachment> getObjectIdByContractId(Long id) {
		if (id == null) {
			return null;
		}
		return quoteContractAttachmentRepository.findQuoteContract(id);
	}
}
