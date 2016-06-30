package com.snowstore.pontus.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * 项目名称：pontus-domain 类名称：ValidateCode 类描述：会员验证码 创建人：admin 创建时间：2016年5月4日
 * 上午10:34:17 修改人：admin 修改时间：2016年5月4日 上午10:34:17 修改备注：
 * 
 * @version
 * 
 */
@Entity
@Table(name = "pontus_validate_code")
@EntityListeners(AuditingEntityListener.class)
public class ValidateCode extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6006470541306838083L;
	private String code;// 验证码
	private String scene;// 场景：注册，修改密码
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;// 客户
	private String system;// 发送系统：app,web

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public static enum Scene {
		REGISTER("注册", "01"), RETRIEVE("找回密码", "02"), SIGN("签署协议", "03"), AUDIT("审核", "04");

		private String value;

		private String code;

		public String getCode() {
			return code;
		}

		private Scene(String value, String code) {
			this.code = code;
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@JsonCreator
		public static Scene fromString(String code) {
			for (Scene scene : Scene.values()) {
				if (scene.getCode().equals(code))
					return scene;
			}
			return REGISTER;

		}

	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum System {
		APP("app"), WEB("web");

		private String value;

		private System(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

	}
}
