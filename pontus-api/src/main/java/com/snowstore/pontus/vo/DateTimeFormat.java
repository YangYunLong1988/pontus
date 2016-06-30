package com.snowstore.pontus.vo;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 
 * 提供对日期时间格式字段的校验注释(默认格式:yyyy-MM-dd)
 * 
 * @author sm
 */
@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = DateTimeFormatProcessor.class)
public @interface DateTimeFormat {

	String message() default "日期格式不正确";

	String pattern() default "yyyy-MM-dd";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
