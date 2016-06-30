package com.snowstore.pontus.service;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.snowstore.pontus.service.utils.DateUtils;

@Service
public class SequenceService {

	private final static String SEQ_MESSAGE_SEQUENCE = "SEQ_MESSAGE_SEQUENCE";
	private final static String SEQ_TRANSFER_SEQUENCE = "SEQ_TRANSFER_SEQUENCE";
	private final static String SEQ_ASSIGNEE_SEQUENCE = "SEQ_ASSIGNEE_SEQUENCE";
	private final static String PREFIX_TRANSFER = "Z";// 出让人编码前缀
	private final static String PREFIX_ASSIGNEE = "S";// 受让人编码前缀

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * 
	 * 从数据库中取Sequence
	 *
	 */
	private String nextSequenceValue(String seqName) {
		String sql = "select " + seqName + ".nextval seq from dual";
		Query query = entityManager.createNativeQuery(sql);
		return query.getSingleResult().toString();
	}

	public String nextEsbMessageSequence() {
		return nextSequenceValue(SEQ_MESSAGE_SEQUENCE);
	}

	public String nextTransferCode() {

		return PREFIX_TRANSFER + DateUtils.dateToString(new Date(), "yyyyMMdd") + StringUtils.leftPad(nextSequenceValue(SEQ_TRANSFER_SEQUENCE), 7, "0");

	}

	public String nextAssigneeCode() {
		return PREFIX_ASSIGNEE + DateUtils.dateToString(new Date(), "yyyyMMdd") + StringUtils.leftPad(nextSequenceValue(SEQ_ASSIGNEE_SEQUENCE), 9, "0");
	}
}
