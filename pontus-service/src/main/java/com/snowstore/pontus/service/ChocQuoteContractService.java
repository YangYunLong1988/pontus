package com.snowstore.pontus.service;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.domain.QuoteContractAttachment;
import com.snowstore.pontus.enums.Enums;
import com.snowstore.pontus.enums.Enums.QuoteContractWorkFlow;
import com.snowstore.pontus.repository.QuoteContractRepository;
import com.snowstore.pontus.service.vo.AddQuoteContractForm;

@Service
@Transactional
public class ChocQuoteContractService {

	@Autowired
	private QuoteContractRepository quoteContractRepository;
	@Autowired
	private QuoteContractService quoteContractService;
	@Autowired
	private QuoteContractAttachmentService quoteContractAttachmentService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private DozerBeanMapper dozerBeanMapper;

	public QuoteContract saveQuoteContract(AddQuoteContractForm addQuoteContractForm, Long id, List<Long> attacIds) {
		QuoteContract quoteContract = null;
		if (id != null) {
			quoteContract = quoteContractService.get(id);
		} else {
			List<String> workFlows = new ArrayList<String>();
			workFlows.add(Enums.QuoteContractWorkFlow.PASSED.getValue());
			workFlows.add(Enums.QuoteContractWorkFlow.PENDING.getValue());
			workFlows.add(Enums.QuoteContractWorkFlow.REJECT.getValue());
			List<QuoteContract> quoteContracts = quoteContractRepository.findByContractCodeAndCustomerIdAndWorkFlows(addQuoteContractForm.getContractCode(), addQuoteContractForm.getCustomerId(), workFlows);
			if (null != quoteContracts && 0 > quoteContracts.size()) {
				throw new PontusServiceException("合同已存在!");
			}
			quoteContract = new QuoteContract();
		}
		dozerBeanMapper.map(addQuoteContractForm, quoteContract);
		quoteContract.setCustomer(customerService.get(addQuoteContractForm.getCustomerId()));
		quoteContract.setWorkFlow(QuoteContractWorkFlow.PENDING.getValue());

		if (Enums.QuoteContracState.INVALID.getValue().equals(quoteContract.getState())) {
			return null;
		}

		quoteContractRepository.save(quoteContract);

		for (QuoteContractAttachment attac : quoteContractAttachmentService.loadQuoteContractAttachment(quoteContract)) {
			if (attacIds.contains(attac.getId())) {
				attacIds.remove(attac.getId());
			} else {
				quoteContractAttachmentService.delete(attac);
			}
		}

		// 绑定附件
		for (Long attacId : attacIds) {
			if (attacId != null) {
				quoteContractAttachmentService.bind(attacId, quoteContract.getId());
			}
		}
		return quoteContract;
	}
}
