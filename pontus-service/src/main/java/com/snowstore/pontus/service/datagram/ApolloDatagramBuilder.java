package com.snowstore.pontus.service.datagram;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snowstore.hera.connector.vo.apollo.D300001;
import com.snowstore.hera.connector.vo.apollo.Protocol;
import com.snowstore.pontus.domain.RenewalAgreement;
import com.snowstore.pontus.service.PropertiesService;
import com.snowstore.pontus.service.SequenceService;
import com.snowstore.pontus.service.constants.PontusConstant;
import com.zendaimoney.hera.connector.vo.Datagram;
import com.zendaimoney.hera.connector.vo.DatagramHeader;

@Component
public class ApolloDatagramBuilder {
	@Autowired
	private PropertiesService propertiesService;
	@Autowired
	private SequenceService sequenceService;

	DatagramHeader createDatagramHeader() {
		DatagramHeader datagramHeader = new DatagramHeader();
		Map<String, String> apolloConfig = propertiesService.getApolloSystemConfiguration();
		datagramHeader.setMessageSequence(sequenceService.nextEsbMessageSequence());
		datagramHeader.setSenderSystemCode(apolloConfig.get("selfSystemCode"));
		datagramHeader.setReceiverSystem(apolloConfig.get("apolloSystemCode"));
		datagramHeader.setUserName(apolloConfig.get("apolloSystemName"));
		datagramHeader.setPassword(apolloConfig.get("apolloSystemPassword"));
		datagramHeader.setSendTime(new Date());
		datagramHeader.setMessageCode("300001");
		return datagramHeader;
	}

	public Datagram buildSealRequest(RenewalAgreement pdf) {
		Datagram datagram = new Datagram();
		datagram.setDatagramHeader(createDatagramHeader());
		D300001 datagramBody = new  D300001();
		Protocol protocol = new Protocol();
		protocol.setId(pdf.getObjectId());
		protocol.setName(pdf.getFileName());

		Set<String> identitys = new HashSet<String>();
		identitys.add(PontusConstant.KL_IDENTITY);
		datagramBody.setIdentitys(identitys);
		datagramBody.setProtocol(protocol);
		datagram.setDatagramBody(datagramBody);
		return datagram;

	}

}
