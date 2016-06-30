package com.snowstore.pontus.service.vo;

import com.snowstore.pontus.domain.ValidateCode.Scene;

public class SendValidateCodeForm {

	private String phone;

	private Long customerId;
	private Scene scene;
	private com.snowstore.pontus.domain.ValidateCode.System system;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public com.snowstore.pontus.domain.ValidateCode.System getSystem() {
		return system;
	}

	public void setSystem(com.snowstore.pontus.domain.ValidateCode.System system) {
		this.system = system;
	}

}
