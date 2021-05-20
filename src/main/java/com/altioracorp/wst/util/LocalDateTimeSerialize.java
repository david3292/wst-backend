package com.altioracorp.wst.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateTimeSerialize extends JsonSerializer<LocalDateTime> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Override
	public void serialize(LocalDateTime ldt, JsonGenerator generator, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		final String dateString = ldt.format(formatter);
		generator.writeString(dateString);
		
	}
}
