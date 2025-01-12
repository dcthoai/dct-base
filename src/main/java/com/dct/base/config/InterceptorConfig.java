package com.dct.base.config;

import com.dct.base.constants.SecurityConstants;
import com.dct.base.constants.BaseConstants;
import com.dct.base.interceptor.BaseHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Used to register and configure Interceptor for HTTP requests in web applications
 * @author thoaidc
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(InterceptorConfig.class);
    private final BaseHandlerInterceptor baseHandlerInterceptor; // A self-defined custom interceptor

    public InterceptorConfig(BaseHandlerInterceptor baseHandlerInterceptor) {
        this.baseHandlerInterceptor = baseHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("Registering base handler interceptor");
        // Register a BaseHandlerInterceptor to handle requests (HTTP requests) before they are passed to the Controllers
        registry.addInterceptor(baseHandlerInterceptor)
                // Ignore some paths that don't need to be processed, such as:
                // Static files (favicon.ico, /images/**)
                // Paths related to the login page (**/login/**)
                // Error or internationalization files (/error**, /i18n/**)
                .excludePathPatterns(BaseConstants.INTERCEPTOR_EXCLUDED_PATHS);
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(SecurityConstants.CORS.ALLOWED_ORIGIN_PATTERNS));
        config.setAllowedHeaders(List.of(SecurityConstants.CORS.ALLOWED_HEADERS));
        config.setAllowedMethods(List.of(SecurityConstants.CORS.ALLOWED_REQUEST_METHODS));
        config.setAllowCredentials(SecurityConstants.CORS.ALLOW_CREDENTIALS);  // Allow sending cookies or authentication information

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(SecurityConstants.CORS.APPLY_FOR, config); // Apply CORS to all endpoints

        return new CorsFilter(source);
    }
}
