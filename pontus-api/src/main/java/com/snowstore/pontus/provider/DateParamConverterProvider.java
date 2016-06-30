package com.snowstore.pontus.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;

import org.apache.commons.lang3.time.FastDateFormat;

import com.snowstore.pontus.service.PontusServiceException;

//@Provider
public class DateParamConverterProvider implements ParamConverterProvider {

	private final String format = "yyyy-MM-dd";

	private final FastDateFormat dateFormat = FastDateFormat.getInstance(format);

	@SuppressWarnings("unchecked")
	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {

		if (rawType != Date.class) {
			return null;
		}

		return (ParamConverter<T>) new ParamConverter<Date>() {

			@Override
			public Date fromString(String value) {
				try {
					return dateFormat.parse(value);
				} catch (Exception ex) {
					throw new PontusServiceException("日期格式不正确");
				}
			}

			@Override
			public String toString(Date date) {
				return dateFormat.format(date);
			}
		};
	}
}
