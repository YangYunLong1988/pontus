package com.snowstore.pontus.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 数据字典表
 */

@Entity
@Table(name = "pontus_dictionary")
@EntityListeners(AuditingEntityListener.class)
public class Dictionary extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6712861931418434170L;
	private String dataType; // 业务码一级码
	private String dataCode;// 业务码
	private String dataName;// 业务码名字
	private String status;// 业务码状态

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataCode() {
		return this.dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	public String getDataName() {
		return this.dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
