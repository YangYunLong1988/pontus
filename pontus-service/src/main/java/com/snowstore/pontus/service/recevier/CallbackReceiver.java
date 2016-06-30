package com.snowstore.pontus.service.recevier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zendaimoney.hera.connector.MessageReceiver;
import com.zendaimoney.hera.connector.vo.Datagram;

/**
 * 分销系统报文接收
 * Created by wulinjie on 2016/1/25.
 */
@Service
@Transactional
public class CallbackReceiver implements MessageReceiver {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());


	@Override
	public Datagram receive(Datagram datagram) {
		Datagram response = null;
		LOGGER.info(datagram.toString());
		return response;
	}
}
