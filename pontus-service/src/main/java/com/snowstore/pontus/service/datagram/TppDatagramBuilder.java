package com.snowstore.pontus.service.datagram;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.snowstore.pontus.service.PropertiesService;
import com.snowstore.pontus.service.SequenceService;
import com.zendaimoney.hera.connector.vo.DatagramHeader;

public abstract class TppDatagramBuilder {
	@Autowired
	private PropertiesService propertiesService;
	@Autowired
	private SequenceService sequenceService;

	DatagramHeader createDatagramHeader() {
		DatagramHeader datagramHeader = new DatagramHeader();
		Map<String, String> tppConfig = propertiesService.getTppSystemConfiguration();
		datagramHeader.setMessageSequence(sequenceService.nextEsbMessageSequence());
		datagramHeader.setSenderSystemCode(tppConfig.get("selfSystemCode"));
		datagramHeader.setReceiverSystem(tppConfig.get("tppSystemCode"));
		datagramHeader.setUserName(tppConfig.get("tppSystemName"));
		datagramHeader.setPassword(tppConfig.get("tppSystemPassword"));
		datagramHeader.setSendTime(new Date());
		datagramHeader.setMessageCode(getHeaderMessageCode());
		return datagramHeader;
	}

	/**
	 * 获取tpp传送的消息头
	 * 
	 * @return
	 */
	abstract String getHeaderMessageCode();

}
