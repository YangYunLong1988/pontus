package com.snowstore.pontus.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.pontus.domain.ExtendFileImage;
import com.snowstore.pontus.domain.RenewalAgreement;
import com.snowstore.pontus.repository.ExtendFileImageRepository;
import com.snowstore.pontus.repository.RenewalAgreementRepository;
import com.snowstore.pontus.service.vo.ExtFileImageVo;

@Service
@Transactional
public class ExtendFileImageService {

	@Autowired
	private ExtendFileImageRepository extFileImageRepository;
	@Autowired
	private RenewalAgreementRepository renewalAgreementRepository;
	@Autowired
	private GridfsService gridfsService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtendFileImageService.class);

	/**
	 * 根据协议取出图片
	 * 
	 * @param contractId
	 * @return
	 */
	public ExtFileImageVo getFileImage(RenewalAgreement file, Integer pageNo) {
		ExtFileImageVo res = new ExtFileImageVo();
		ExtendFileImage image = null;
		if (null == file.getTotalPages()) {
			genImage(file);
		}
		image = extFileImageRepository.findByPdfFileAndPageNo(file.getId(), pageNo);
		try {
			String content = gridfsService.getBase64Content(image.getObjectId());
			res.setContent(content);
		} catch (IOException e) {
			LOGGER.error("查询图片异常", e);
		}
		res.setCurNo(pageNo);
		res.setTotalNo(file.getTotalPages());
		return res;
	}

	public void genImage(RenewalAgreement file) {
		try {
			//先删除再新增
			extFileImageRepository.deleleByEnewalAgreementId(file.getId());
			List<byte[]> imageList = genPdfImage(gridfsService.getByteContent(file.getObjectId()));
			file.setTotalPages(imageList.size());
			renewalAgreementRepository.save(file);
			for (int index = 0; index < imageList.size(); index++) {
				ExtendFileImage tmp = new ExtendFileImage();
				tmp.setPageNo(index + 1);
				tmp.setObjectId(gridfsService.saveAndGetObjectId(file.getFileName(), "png", new ByteArrayInputStream(imageList.get(index))));
				tmp.setPdfFile(file);
				extFileImageRepository.save(tmp);
			}
		} catch (IOException e) {
			LOGGER.error("生成图片异常", e);
			throw new PontusServiceException("生成图片异常");
		}
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
}
