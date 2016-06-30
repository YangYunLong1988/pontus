package com.snowstore.pontus.service.vo;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

	private final NumberFormat format;

	public BigDecimalSerializer() {
		format = NumberFormat.getInstance();
		format.setMinimumFractionDigits(2);
		format.setMinimumIntegerDigits(1);
	}

	@Override
	public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(format.format(value));
	}

}
