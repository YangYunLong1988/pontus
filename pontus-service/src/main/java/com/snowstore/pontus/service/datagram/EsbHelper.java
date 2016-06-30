package com.snowstore.pontus.service.datagram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.snowstore.hera.connector.vo.apollo.D300001_R;
import com.snowstore.hera.connector.vo.tpp.D01000004_R;
import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.domain.RenewalAgreement;
import com.zendaimoney.hera.connector.EsbConnector;
import com.zendaimoney.hera.connector.vo.Datagram;

@Component
public class EsbHelper {
	@Autowired
	private EsbConnector esbConnector;
	@Autowired
	private Tpp01000004 tpp01000004;
	@Autowired
	private MarsDatagramBuilder marsDatagramBuilder;
	@Autowired
	private ApolloDatagramBuilder apolloDatagramBuilder;

	/**
	 * 发送中金验证账户请求
	 * 
	 * @param customer
	 * @return
	 */
	public D01000004_R sendTpp01000004(Customer customer, BankInfo bankInfo) {
		Datagram request = tpp01000004.buildDatagram(customer, bankInfo);
		Datagram response = esbConnector.sendAndReceive(request);
		return (D01000004_R) response.getDatagramBody();
	}
	@Async
	public void sendSmsRequest(String content, String mobile, Boolean isValidationCode) {
		Datagram request = marsDatagramBuilder.buildSmsRequest(content, mobile, isValidationCode);
		esbConnector.send(request);
	}

	public D300001_R sendSealRequest(RenewalAgreement pdf) {
		Datagram req = apolloDatagramBuilder.buildSealRequest(pdf);
		return (D300001_R) this.esbConnector.sendAndReceive(req).getDatagramBody();

	}
}
