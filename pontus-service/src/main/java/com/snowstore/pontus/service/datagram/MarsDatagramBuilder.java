package com.snowstore.pontus.service.datagram;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snowstore.hera.connector.vo.mars.MarsMessage;
import com.snowstore.pontus.service.PropertiesService;
import com.snowstore.pontus.service.SequenceService;
import com.zendaimoney.hera.connector.vo.Datagram;
import com.zendaimoney.hera.connector.vo.DatagramHeader;

@Component
public class MarsDatagramBuilder {
	@Autowired
	private PropertiesService propertiesService;
	@Autowired
	private SequenceService sequenceService;

	DatagramHeader createDatagramHeader() {
		DatagramHeader datagramHeader = new DatagramHeader();
		Map<String, String> marsConfig = propertiesService.getMarsSystemConfiguration();
		datagramHeader.setMessageSequence(marsConfig.get("selfSystemCode") + sequenceService.nextEsbMessageSequence());
		datagramHeader.setSenderSystemCode(marsConfig.get("selfSystemCode"));
		datagramHeader.setReceiverSystem(marsConfig.get("marsSystemCode"));
		datagramHeader.setUserName(marsConfig.get("marsSystemName"));
		datagramHeader.setPassword(marsConfig.get("marsSystemPassword"));
		datagramHeader.setSendTime(new Date());
		datagramHeader.setMessageCode("MarsMessage");
		return datagramHeader;
	}

	public Datagram buildSmsRequest(String content, String mobile, Boolean isValidationCode) {
		Datagram datagram = new Datagram();
		datagram.setDatagramHeader(createDatagramHeader());
		MarsMessage datagramBody = new MarsMessage();
		datagramBody.setTo(mobile);
		datagramBody.setFrom("28");
		datagramBody.setContent(content);
		datagramBody.setOprOption("4");
		datagramBody.setSubject("1");
		datagramBody.setMessageType(isValidationCode ? "1" : "2");// 验证码
		datagram.setDatagramBody(datagramBody);
		return datagram;

	}

}
