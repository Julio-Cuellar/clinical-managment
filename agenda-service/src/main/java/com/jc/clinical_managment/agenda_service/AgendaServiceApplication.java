package com.jc.clinical_managment.agenda_service;

import org.luminiaClinical.security.JwtAuthenticationFilter;
import org.luminiaClinical.security.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgendaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendaServiceApplication.class, args);
	}


	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
		FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		String secret = "TU_SECRETO_COMPARTIDO_CON_AUTH_SERVICE"; // Usa variable de entorno en producción
		registrationBean.setFilter(new JwtAuthenticationFilter(new JwtUtils(secret)));
		registrationBean.addUrlPatterns("/api/*"); // Protege los endpoints bajo /api/
		return registrationBean;
	}

}
