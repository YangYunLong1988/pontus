package com.snowstore.pontus.service.datagram;

import org.springframework.stereotype.Component;

import com.snowstore.hera.connector.vo.tpp.D01000004;
import com.snowstore.pontus.domain.BankInfo;
import com.snowstore.pontus.domain.Customer;
import com.snowstore.pontus.service.common.BankCode;
import com.snowstore.pontus.service.common.CityCode;
import com.snowstore.pontus.service.common.ProvinceCode;
import com.snowstore.pontus.service.constants.TppConstant;
import com.zendaimoney.hera.connector.vo.Datagram;

@Component
public class Tpp01000004 extends TppDatagramBuilder {

	@Override
	String getHeaderMessageCode() {
		return TppConstant.BusinessType.VERIFICATION_ACCOUNT;
	}

	public Datagram buildDatagram(Customer customer,BankInfo bankInfo) {
		Datagram datagram = new Datagram();
		datagram.setDatagramHeader(createDatagramHeader());
		D01000004 d01000004 = new D01000004();
		d01000004.setIsReSend("0");// 是否重发
		d01000004.setAsynTag("0");// 是否异步
		d01000004.setThirdPartyType(TppConstant.PayChannel.ZJ);
		d01000004.setRequestOprator("pontus");
		d01000004.setBankCode(BankCode.getCode(bankInfo.getName()));
		d01000004.setAccountType(TppConstant.AccountType.PERSONAL);
		d01000004.setAccountName(customer.getIdCardName());
		d01000004.setAccountNumber(bankInfo.getAccount());
		d01000004.setChName(bankInfo.getSubbranch());
		d01000004.setProvince(ProvinceCode.getCode(bankInfo.getProvince()));
		d01000004.setCity(CityCode.getCode(bankInfo.getCity(), bankInfo.getProvince()));
		d01000004.setIdentificationType(TppConstant.IdentificationType.IDCARD);
		d01000004.setIdentificationNumber(customer.getIdCardAccount());
		datagram.setDatagramBody(d01000004);
		return datagram;
	}
}
