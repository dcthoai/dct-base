package com.dct.base.config;

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
                .excludePathPatterns("**/favicon.ico")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("**images**")
                .excludePathPatterns("/index.html")
                .excludePathPatterns("**index.html**")
                .excludePathPatterns("**/file/**")
                .excludePathPatterns("**/login/**")
                .excludePathPatterns("/error**")
                .excludePathPatterns("/i18n/**");
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOriginPattern("*");
        config.setAllowedHeaders(List.of(
                "Content-Type",     // Content format
                "Authorization",    // Authentication token
                "Accept",           // Client-expected content
                "Origin",           // Origin of the request
                "X-CSRF-Token",     // Anti-CSRF token
                "X-Requested-With", // Ajax request markup
                "Access-Control-Allow-Origin", // Server response header
                "X-App-Version",    // Application version (optional)
                "X-Device-ID"
        ));

        config.setAllowedMethods(List.of("GET", "PUT", "POST", "DELETE"));
        config.setAllowCredentials(true);  // Allow sending cookies or authentication information

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply CORS to all endpoints

        return new CorsFilter(source);
    }
}
