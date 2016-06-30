package com.snowstore.pontus.vo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.snowstore.pontus.domain.ValidateCode.Scene;

/**
 * @author sm
 * 
 */
public class EnumIncluderProcessor implements ConstraintValidator<EnumInclude, Scene> {

	private Set<Scene> scenes;

	@Override
	public void initialize(EnumInclude constraintAnnotation) {
		scenes = new HashSet<>(Arrays.asList(constraintAnnotation.scene()));

	}

	@Override
	public boolean isValid(Scene value, ConstraintValidatorContext context) {
		return scenes.contains(value);
	}
}
