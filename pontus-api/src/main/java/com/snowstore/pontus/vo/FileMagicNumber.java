package com.snowstore.pontus.vo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.snowstore.pontus.vo.FileMagicNumberProcessor.FileType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = FileMagicNumberProcessor.class)
public @interface FileMagicNumber {

	FileType[] fileTypes() default { FileType.PNG, FileType.JPG };

	String message() default "图片格式不正确";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
