package com.dct.base.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * In Spring, {@link LocaleResolver} determines the current language of the application based on the HTTP request. <p>
 * {@link AcceptHeaderLocaleResolver} automatically analyzes the value of the Accept-Language header in each request
 * and selects the locale <p>
 * This {@link Locale} value is used to retrieve internationalized messages (I18n)
 * @author thoaidc
 */
@Configuration
public class LocaleConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LocaleConfiguration.class);

    @Bean(name = "localeResolver")
    public LocaleResolver localeResolver() {
        log.debug("Allow accept-header to set locale for http response");
        return new AcceptHeaderLocaleResolver();
    }
}
