package com.snowstore.pontus.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.snowstore.hera.connector.vo.apollo.D300001_R;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.domain.RenewalAgreement;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.repository.QuoteContractRepository;
import com.snowstore.pontus.repository.RenewalAgreementRepository;
import com.snowstore.pontus.service.constants.TppConstant;
import com.snowstore.pontus.service.datagram.EsbHelper;
import com.snowstore.pontus.service.pdf.PdfFactory;

@Service
@Transactional
public class RenewalAgreementService {
	@Autowired
	private GridFsTemplate gridFsTemplate;
	@Autowired
	private PdfFactory pdfFactory;
	@Autowired
	private RenewalAgreementRepository renewalAgreementRepository;
	@Autowired
	private EsbHelper esbHelper;
	@Autowired
	private ExtendFileImageService extendFileImageService;
	@Autowired
	private QuoteContractRepository quoteContractRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(RenewalAgreementService.class);

	public void sign(Long quoteContractId) {

		QuoteContract quoteContract = quoteContractRepository.findOne(quoteContractId);
		RenewalAgreement renewalAgreement = renewalAgreementRepository.findQuoteContract(quoteContractId);
		// 重新生成协议
		byte[] bytes = pdfFactory.buildKLSupplementaryFile(quoteContract, true);
		// 保存monmgoDB
		GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), "", "application/pdf");
		renewalAgreement.setObjectId(gridFile.getId().toString());

		D300001_R ret = esbHelper.sendSealRequest(renewalAgreement);
		LOGGER.debug("签约系统返回码【" + ret.getOperateCode() + "】，签约系统返回memo【" + ret.getMemo() + "】");
		if (ret.getOperateCode().equals(TppConstant.ResponseCode.SUCCESS)) {
			// 重新生成图片接口
			renewalAgreement.setObjectId(ret.getNewProtocolId());
			extendFileImageService.genImage(renewalAgreement);
			// 更新展期新的协议objectid
			renewalAgreementRepository.save(renewalAgreement);
			// 更新挂牌合同签署字段为已签署
			quoteContract.setSignState(true);
			quoteContract.setWorkFlow(Enums.QuoteContractWorkFlow.RENEWED.getValue());
			quoteContractRepository.save(quoteContract);
		} else {
			throw new PontusServiceException("签约系统返回码[" + ret.getOperateCode() + "],返回memo[" + ret.getMemo() + "]");
		}
	}

	public RenewalAgreement findByQuoteContract(Long quoteContractId) {
		return renewalAgreementRepository.findQuoteContract(quoteContractId);
	}

	RenewalAgreement genExtFile(QuoteContract quoteContract, Boolean sealSupported) {
		// 生成协议
		byte[] bytes = pdfFactory.buildKLSupplementaryFile(quoteContract, sealSupported);
		// 保存monmgoDB
		GridFSFile gridFile = gridFsTemplate.store(new ByteArrayInputStream(bytes), "", "application/pdf");
		// 生成展期协议
		RenewalAgreement agreement = new RenewalAgreement();
		agreement.setQuoteContract(quoteContract);
		agreement.setObjectId(gridFile.getId().toString());
		return renewalAgreementRepository.save(agreement);
	}

	public RenewalAgreement get(Long id) {
		return renewalAgreementRepository.findOne(id);
	}

	public byte[] getContent(RenewalAgreement attachment) {
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

	private GridFSDBFile getGridFSDBFile(RenewalAgreement attachment) {
		if (attachment == null || attachment.getObjectId() == null) {
			return null;
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(attachment.getObjectId())));
		return gridFsTemplate.findOne(query);
	}
}
