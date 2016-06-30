package com.snowstore.pontus.service.constants;

import java.util.ArrayList;
import java.util.List;

public class PontusConstant {
	public final static String KL_IDENTITY = "KL";
	public static final String ANONYMOUS_USER = "anonymousUser";
	public static final String SUBMIT_REPEATEDLY = "SUBMIT_REPEATEDLY";

	public final static List<String> urls = new ArrayList<>();

	static {
		// IndexController
		urls.add("/");
		urls.add("/login");
		urls.add("/register");
		urls.add("/forget-pwd-one");
		urls.add("/forget-pwd-two");
		urls.add("/forget-pwd-three");
		urls.add("/forget-pwd-four");
		urls.add("/regist-id-auth");
		urls.add("/regist-id-success");
		urls.add("/submit-auth");

		// CustomerController
		urls.add("/check-img-code");
		urls.add("/send-phone-code");
		urls.add("/send-phone-code2");
		urls.add("/check-phone-vcode");
		urls.add("/check-unique-phone");
		urls.add("/check-register-phone");

		urls.add("/cust/identify-success");
		urls.add("/cust/check-unique-idcard");
		urls.add("/cust/submit-auth");
		urls.add("/cust/upload");
		urls.add("/cust/thumbnail");

		urls.add("/attachment/registerprotocal.pdf");

		urls.add("/kaptcha");

		urls.add("/not-found");
	}
}
