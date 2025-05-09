package com.dct.base.config;

import com.dct.base.constants.BaseSecurityConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Used to register and configure Interceptor for HTTP requests in web applications
 * @author thoaidc
 */
@Configuration
public class BaseCorsFilterConfigurer {

    private static final Logger log = LoggerFactory.getLogger(BaseCorsFilterConfigurer.class);

    /**
     * Configures the CORS (Cross-Origin Resource Sharing) filter in the application <p>
     * CORS is a security mechanism that allows or denies requests between different origins <p>
     * View the details of the permissions or restrictions in {@link BaseSecurityConstants.CORS}
     */
    @Bean
    public CorsFilter corsFilter() {
        log.debug("Configure default CorsFilter bean");
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(BaseSecurityConstants.CORS.ALLOWED_ORIGIN_PATTERNS));
        config.setAllowedHeaders(List.of(BaseSecurityConstants.CORS.ALLOWED_HEADERS));
        config.setAllowedMethods(List.of(BaseSecurityConstants.CORS.ALLOWED_REQUEST_METHODS));
        config.setAllowCredentials(BaseSecurityConstants.CORS.ALLOW_CREDENTIALS);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(BaseSecurityConstants.CORS.APPLY_FOR, config); // Apply CORS to all endpoints

        return new CorsFilter(source);
    }
}
