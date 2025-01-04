package com.dct.base.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class InterceptorConfig {

    private static final Logger log = LoggerFactory.getLogger(InterceptorConfig.class);


    @Bean
    public CorsFilter corsFilter() {
        log.debug("Registering CORS filter");
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
