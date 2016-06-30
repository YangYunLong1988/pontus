package com.snowstore.pontus.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * 项目名称：pontus-domain 类名称：BankInfo 类描述：会员银行信息表 创建人：admin 创建时间：2016年5月4日
 * 上午10:36:30 修改人：admin 修改时间：2016年5月4日 上午10:36:30 修改备注：
 * 
 * @version
 *
 */
@Entity
@Table(name = "pontus_bank_info")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "customer" })
public class BankInfo extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6253436384187520827L;

	private String name;// 银行名称
	private String account;// 账号
	private String subbranch;// 支行，eg.南京东路支行
	private String province;// 分支行省份(企业结算需要)
	private String city;// 分支行城市(企业结算需要)
	private String status;// 银行卡状态
	private Boolean defaulted = Boolean.FALSE;// 是否默认银行卡
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;// 所属会员

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSubbranch() {
		return subbranch;
	}

	public void setSubbranch(String subbranch) {
		this.subbranch = subbranch;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Boolean getDefaulted() {
		return defaulted;
	}

	public void setDefaulted(Boolean defaulted) {
		this.defaulted = defaulted;
	}

	private static final Map<String, String> CSSTRANSFER = new HashMap<>();

	static {
		CSSTRANSFER.put("中国工商银行", "gongshang");
		CSSTRANSFER.put("中国农业银行", "nogye");
		CSSTRANSFER.put("中国银行", "zhongguo");
		CSSTRANSFER.put("中国建设银行", "jianshe");
		CSSTRANSFER.put("兴业银行", "xingye");
		CSSTRANSFER.put("中国光大银行", "guangda");
		CSSTRANSFER.put("中国邮政储蓄银行", "youzheng");
		CSSTRANSFER.put("深圳平安银行", "pingan");
		CSSTRANSFER.put("中信银行", "zhongxin");
		CSSTRANSFER.put("上海银行", "shanghai");
		CSSTRANSFER.put("北京银行", "bingjing");
		CSSTRANSFER.put("交通银行", "jiaotong");
		CSSTRANSFER.put("广东发展银行", "guangfa");
		CSSTRANSFER.put("中国民生银行", "minsheng");

	}

	@Transient
	public String getCssClass() {
		String result = CSSTRANSFER.get(name);
		if (null == result) {
			result = "";
		}
		return result;
	}
}
