package com.snowstore.pontus.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

public class Enums {

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum AttachType {
		/** 身份证正面 **/
		IDCARD_POSITIVE("IDCARD_POSITIVE"),
		/** 身份证反面 **/
		IDCARD_OPPOSITE("IDCARD_OPPOSITE"),
		/** 本人持身份证 **/
		IDCARD_PERSON("IDCARD_PERSON"),
		// /** 合同附件 **/
		// CARD_CONTRACT("CARD_CONTRACT"),
		/** 名片 **/
		CARD_PERSON("CARD_PERSON"),
		/** 展期协议 **/
		RENEWAL_PROTOCOL("RENEWAL_PROTOCOL"),
		/** 担保承诺函协议 **/
		GUARANTEE_PROTOCOL("GUARANTEE_PROTOCOL"),
		/** 债权转让协议 **/
		TRANSFER_PROTOCOL("TRANSFER_PROTOCOL"),
		/** 收款确认函协议 **/
		CONFIRM_PROTOCOL("CONFIRM_PROTOCOL"),
		/** 支付凭证 **/
		PAY_PROTOCOL("PAY_PROTOCOL"),
		/** 债权转让通知函 **/
		TRANSFER_NOTIFY_PROTOCOL("TRANSFER_NOTIFY_PROTOCOL");

		private String value;

		private AttachType(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum AttachTableAlias {
		/** 合同附件表 **/
		QUOTECONTRACTATTACHMENT("1"),
		/** 受让附件 **/
		ASSIGNEEATTACHMENT("2");

		private String value;

		private AttachTableAlias(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum Charset {

		ISO88591("ISO8859-1"), UTF8("UTF-8"), GBK("GBK");
		private String value;

		private Charset(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum BranchBankState {

		NORMAL("正常");
		private String value;

		private BranchBankState(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum BankInfoState {

		PASSED("有效"), REJECT("无效");

		private String value;

		private BankInfoState(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum CustomerState {
		VALID("有效"), INVALID("无效");

		private String value;

		private CustomerState(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum OriginalContractWorkFlow{
		BOUND("已绑定");

		private String value;

		private OriginalContractWorkFlow(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum OriginalContractState {
		VALID("有效"), INVALID("无效");

		private String value;

		private OriginalContractState(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum CustomerWorkFlow {
		REGISTER("注册"), REJECT("认证失败"), PASSED("认证通过");

		private String value;

		private CustomerWorkFlow(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum QuoteContractWorkFlow {
		/** 新建 **/
		NEW("新建"),
		/** 待审核 **/
		PENDING("待审核"),
		/** 未通过 **/
		REJECT("未通过"),
		/** 已通过 **/
		PASSED("已通过"),
		/** 已展期 ,表示 可转让 **/
		RENEWED("已展期"),
		/** 转让中 **/
		TRANSFERING("转让中"),
		// /** 交易中 有预约的 **/
		// DEALING("交易中"),
		/** 交易成功 **/
		SUCCESS("交易成功");

		private String value;

		private QuoteContractWorkFlow(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}

	}

	/**
	 * 证件类型
	 * 
	 * @Project: pontus-domain
	 * @Author zy
	 * @Company:
	 * @Create Time: 2016年5月9日 上午9:24:21
	 */
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum CertiType {
		ID_CARD("身份证");

		private String value;

		private CertiType(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}

	}

	public enum Platform {
		易联天下, 金鹿财行, 当天财富, 玖玖金服, 翰典, 趣逗理财, 翎秀, 魔环, 基冉, 宝驼贷, 极陀, 其它
	}

	/**
	 *
	 * 项目名称：pontus-domain 类名称：AssigneeWorkFlow 类描述：受让人状态 创建人：admin 创建时间：2016年5月13日 下午4:58:42 修改人：admin 修改时间：2016年5月13日 下午4:58:42 修改备注：
	 * 
	 * @version
	 *
	 */
	/**
	 *
	 * 项目名称：pontus-domain 类名称：AssigneeWorkFlow 类描述：受让人附件状态 创建人：admin 创建时间：2016年5月13日 下午4:58:57 修改人：admin 修改时间：2016年5月13日 下午4:58:57 修改备注：
	 * 
	 * @version
	 *
	 */
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum AssigneeWorkFlow {
		/** 1、 客户登记（前台预约暂时未做客服回访） **/
		APPOINT("客户登记"),
		/** 2、 回访取消（客服回访后客户取消受让） **/
		REJECT("回访取消"),
		/** 3、 回访同意（客服回访后客户同意受让） **/
		AGREE("回访同意"),
		/** 4、 交易完成（转让受让线下交易完成后上传附件至后台运营审核通过并提交交易完成 **/
		SUCCESS("交易完成");

		private String value;

		private AssigneeWorkFlow(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}

	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum AssigneeWorkAttachFlow {
		/** 1、 未上传 **/
		NONE("无"),
		/** 1、 未上传 **/
		UNDO("未上传"),
		/** 2、 待审核 ：当用户前台上传新的附件时，后台>>交易管理>>转让管理列表更新状态并显示“新”的图标，提醒运营人员 **/
		PENDING("待审核"),
		/** 3、 审核通过 （后置是运营提交“交易完成”） **/
		SUCESS("审核通过"),
		/** 4、 审核回退 （用户前台可再次补件上传） **/
		REJECT("审核回退");

		private String value;

		private AssigneeWorkAttachFlow(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}

	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum TransferFlow {
		/** 1、 转让中（0人预约） **/
		PENDING("转让中"),
		/** 2、 交易中 预约人数>=1 **/
		TRADING("交易中"),
		/** 2、 交易确认中 点击回访同意触发 **/
		ENSURE("交易确认中"),
		/** 3、 已取消（用户前台主动“取消转让”） **/
		CANCEL("已取消"),
		/** 4、 回访取消（运营客服回访后台“取消转让”） **/
		REJECT("回访取消"),
		/** 5、 已交易（交易完成） **/
		SUCCESS("已交易");

		private String value;

		private TransferFlow(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}

	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum TransferState {
		VALID("有效"), INVALID("无效");

		private String value;

		private TransferState(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum QuoteContracState {
		VALID("有效"), INVALID("无效");

		private String value;

		private QuoteContracState(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum AssigneeState {
		VALID("有效"), INVALID("无效");

		private String value;

		private AssigneeState(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum AssigneeAttachmentState {
		VALID("有效"), INVALID("无效");

		private String value;

		private AssigneeAttachmentState(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum RepaymentMode {
		/** 到期还本付息 */
		END("到期还本付息"),
		/** 每月付息到期还本 */
		MONTH("每月付息到期还本"),
		/** 每季付息到期还本 */
		SEASON("每季付息到期还本"),
		/** 半年付息到期还本 */
		SEMESTER("半年付息到期还本");

		private String value;

		private RepaymentMode(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum TransferSortRule {
		/** 折价率升序 */
		DISCOUNTRATE_ASC("DISCOUNTRATE_ASC"),
		/** 折价率降序 */
		DISCOUNTRATE_DESC("DISCOUNTRATE_DESC"),
		/** 转让价格升序 */
		TRANSFERPRICE_ASC("TRANSFERPRICE_ASC"),
		/** 转让价格降序 */
		TRANSFERPRICE_DESC("TRANSFERPRICE_DESC"),
		/** 预约人数降序 */
		ASSIGNEE_DESC("ASSIGNEE_DESC");

		private String value;

		private TransferSortRule(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	/**
	 * 判断字符串是不是枚举value，枚举中需修改toString
	 * 
	 * @param enumClass
	 * @param enumValue
	 * @return
	 */
	public static <E extends Enum<E>> boolean isValidEnumValue(final Class<E> enumClass, final String enumValue) {
		if (enumValue == null) {
			return false;
		}
		try {
			for (final E e : enumClass.getEnumConstants()) {
				if (enumValue.equals(e.toString())) {
					return true;
				}
			}
			return false;
		} catch (final IllegalArgumentException ex) {
			return false;
		}
	}

	/**
	 * 判断名字是不是给定枚举
	 * 
	 * @param enumClass
	 * @param enumName
	 * @return
	 */
	public static <E extends Enum<E>> boolean isValidEnumName(final Class<E> enumClass, final String enumName) {
		if (enumName == null) {
			return false;
		}
		try {
			Enum.valueOf(enumClass, enumName);
			return true;
		} catch (final IllegalArgumentException ex) {
			return false;
		}
	}

}
