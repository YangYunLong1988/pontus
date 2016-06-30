package com.snowstore.pontus.vo;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.util.StringUtils;

/**
 * @author sm
 *
 */
public class DateTimeFormatProcessor implements ConstraintValidator<DateTimeFormat, String> {

	private String pattern;

	FastDateFormat fastDateFormat;

	@Override
	public void initialize(DateTimeFormat constraintAnnotation) {
		pattern = constraintAnnotation.pattern();
		fastDateFormat = FastDateFormat.getInstance(pattern);

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (StringUtils.isEmpty(value))
			return false;

		try {
			fastDateFormat.parse(value);
		} catch (Exception e) {
			return false;
		}
		return Pattern.matches("^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$", value);

	}
}
