package com.snowstore.pontus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snowstore.pontus.domain.OriginalContract;
import com.snowstore.pontus.domain.QuoteContract;
import com.snowstore.pontus.repository.OriginalContractRepository;
import com.snowstore.pontus.repository.QuoteContractRepository;

@Service
@Transactional
public class OriginalContractService {
	@Autowired
	private OriginalContractRepository originalContractRepository;
	@Autowired
	private QuoteContractRepository quoteContractRepository;

	public OriginalContract findByQuoteContract(QuoteContract quoteContract) {
		return originalContractRepository.findByQuoteContract(quoteContract);
	}

	public OriginalContract get(Long id) {
		return originalContractRepository.findOne(id);
	}

	public OriginalContract findByContractCode(String contractCode) {
		return originalContractRepository.findByContractCode(contractCode);
	}

	public List<Long> findByCustomerId(Long customerId) {
		return quoteContractRepository.findByCustomerId(customerId);
	}
}
