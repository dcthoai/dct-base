package com.dct.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfiguration {

    private static final Logger log = LoggerFactory.getLogger(HttpClientConfiguration.class);

    /**
     * This configuration defines a RestTemplate bean in Spring, which is used to make HTTP requests <p>
     * Purpose: Create an instance of RestTemplate, a tool that makes sending HTTP requests and handling responses
     */
    @Bean
    public RestTemplate restTemplate() {
        log.debug("Configured RestTemplate for send HTTP request/response in spring");
        RestTemplate restTemplate = new RestTemplate();
        // Create an HTTP message converter, using Jackson to convert between JSON and Java objects
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        restTemplate.getMessageConverters().add(converter);
        return restTemplate;
    }
}
