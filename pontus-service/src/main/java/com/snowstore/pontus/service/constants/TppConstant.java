package com.snowstore.pontus.service.constants;

public class TppConstant {
	
	public static final class ResponseCode {
		public static final String SUCCESS = "000000";
	}
	
	/**
	 * 接口业务类型代码
	 * 
	 */
	public static final class BusinessType {
		private BusinessType() {
		}

		/** 验证账户信息 */
		public static final String VERIFICATION_ACCOUNT = "01000004";

	}

	/**
	 *
	 * 类描述：第三方机构
	 */
	public static final class PayChannel {
		private PayChannel() {
		}

		/** 中金支付 */
		public static final String ZJ = "10010001";
	}

	/**
	 *
	 * 类描述：账户类型
	 */
	public static final class AccountType {

		private AccountType() {
		}

		/** 个人 */
		public static final String PERSONAL = "10040001";
		/** 企业 */
		public static final String ENTERPRISE = "10040002";
		/** 支付平台内部账户 */
		public static final String INTERNAL = "10040003";
	}

	/**
	 *
	 * 类描述：证件类型
	 */
	public static final class IdentificationType {
		private IdentificationType() {
		}

		/** 身份证 */
		public static final String IDCARD = "10050000";
		/** 户口簿 */
		public static final String RESIDENCE_BOOKLET = "10050001";
		/** 护照 */
		public static final String PASSPORT = "10050002";
		/** 其他 */
		public static final String OTHERS = "10050010";
	}
}
