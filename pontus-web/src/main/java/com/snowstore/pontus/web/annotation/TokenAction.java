package com.snowstore.pontus.web.annotation;

public enum TokenAction {
	/** 初始化token保存到session */
	PREPARATION,
	/** 提交请求 将会对请求进行重复判断 */
	SUBMIT,
	/** ajax提交请求判断 */
	AJAX_SUBMIT
}
