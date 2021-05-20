package com.altioracorp.wst;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigLocalidad {

	public static final String ZONA_HORARIA = "America/Guayaquil";
	
	@Bean
	public Locale locale() {
		return new Locale("es", "EC");
	}
	
	@Bean
	public Moneda moneda() {
		return new Moneda("USD");
	}
	
	@Bean
	public TimeZone timeZone() {
		return TimeZone.getTimeZone(ZONA_HORARIA);
	}
	
	@Bean
	public ZoneId zoneId() {
		return ZoneId.of(ZONA_HORARIA);
	}
}
