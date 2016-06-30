package com.snowstore.pontus.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class NotNullObjectMapperProvider implements ContextResolver<ObjectMapper> {

	final ObjectMapper mapper;

	public NotNullObjectMapperProvider() {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}
}
