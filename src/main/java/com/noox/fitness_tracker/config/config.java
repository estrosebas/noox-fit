package com.noox.fitness_tracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import java.util.Locale;
@Configuration
public class config {
	@Bean
	public ResourceBundleMessageSource messageSource() {
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasename("messages/messages");
	    messageSource.setDefaultEncoding("UTF-8");
		Locale locale = Locale.forLanguageTag("es");
		messageSource.setDefaultLocale(locale);
	    return messageSource;
	}
}
