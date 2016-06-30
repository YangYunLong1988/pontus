package com.snowstore.pontus.vo;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.snowstore.pontus.domain.ValidateCode.Scene;

/**
 * 
 * 提供对enum的校验
 * 
 * @author sm
 */
@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = EnumIncluderProcessor.class)
public @interface EnumInclude {

	String message() default "不正确";

	Scene[] scene();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
