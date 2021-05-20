package com.altioracorp.wst;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.altioracorp.wst.dominio.EntidadBase;

@Configuration
@EntityScan(basePackageClasses = EntidadBase.class)
@EnableJpaAuditing
@EnableTransactionManagement
public class ConfigAuditoria {

	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAwareEntidades();
	}
	
}
