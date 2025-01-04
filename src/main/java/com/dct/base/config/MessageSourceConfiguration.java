package com.dct.base.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Supports internationalization (i18n) and integration with validation <p>
 * Useful when using Hibernate Validator with annotations like @NotNull, @Size,...
 * @author thoaidc
 */
@Configuration
public class MessageSourceConfiguration {

    @Bean
    public MessageSource messageSource() {
        // Provides a mechanism to load notifications from .properties files to support i18n
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // Set the location of the message files
        // Spring will look for files by name messages_{locale}.properties
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        // Connect the validation to MessageSource to get error messages from message bundles
        // When a validation error occurs, Spring looks for the error message from the .properties files provided
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}