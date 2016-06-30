package com.snowstore.pontus.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止重复提交
 * 
 * <pre>
 * Cotroller：
 * 提交前生成token放到session
 * AvoidDuplicateSubmission(tokenAction = TokenAction.PREPARATION)
 * 提交处理方法判断是否重复提交
 * AvoidDuplicateSubmission(tokenAction = TokenAction.SUBMIT, dupUrl = "/appointment/book")
 * Page：
 * &ltinput type="hidden" name="token" value="${token}" /&gt
 * </pre>
 * 
 * @author vip
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidDuplicateSubmission {
	/** 初始化 token 或 提交表单（重复控制） */
	TokenAction tokenAction();

	int limitTime() default 60;

	/** 重复提交跳转路径 */
	String dupUrl() default "/error";
}