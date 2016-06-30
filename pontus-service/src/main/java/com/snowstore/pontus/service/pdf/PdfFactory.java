package com.snowstore.pontus.service.pdf;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.service.PontusServiceException;

import freemarker.template.TemplateException;

/**
 * 创建各种PDF协议文件
 * 
 * @author xj
 */
@Service
@Transactional
public class PdfFactory {
	/** 快鹿补充担保协议 */
	public static final String KL_SUPPLEMENTARY_GUARANTEE = "kl_supplementary_guarantee";

	public byte[] buildKLSupplementaryFile(QuoteContract contract, Boolean sealSupported) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("customerName", contract.getCustomer().getIdCardName());
			map.put("customerCardType", "身份证");
			map.put("customerCardId", contract.getCustomer().getIdCardAccount());
			map.put("orderCode", contract.getContractCode());
			map.put("platform", contract.getPlatform());
			map.put("createDate", sealSupported ? new Date() : null);
			map.put("guaCode", contract.getContractCode());
			map.put("mobile", contract.getCustomer().getPhone());
			return PDFBuilder.createPDF(KL_SUPPLEMENTARY_GUARANTEE, map);
		} catch (IOException | TemplateException | DocumentException e) {
			throw new PontusServiceException("创建快鹿补充担保协议出错", e);
		}
	}

	// @Test
	// public void tatad() {
	//
	// try {
	// QuoteContract contract = new QuoteContract();
	// contract.setContractCode("23463263246234");
	// contract.setPlatform("上海金鹿金融信息服务有限公司");
	// Customer cc = new Customer();
	//
	// cc.setPhone("13917914017");
	// cc.setIdCardName("孙凯琳挨");
	// cc.setIdCardAccount("320124198809284352");
	// contract.setCustomer(cc);
	// FileUtils.writeByteArrayToFile(new File("f:/协议tsts.pdf"),
	// buildKLSupplementaryFile(contract, false));
	// FileUtils.writeByteArrayToFile(new File("f:/casdsad.pdf"),
	// buildKLSupplementaryFile(contract, true));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
}
