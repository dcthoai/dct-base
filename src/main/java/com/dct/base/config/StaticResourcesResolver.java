package com.dct.base.config;

import com.dct.base.constants.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Static resource handling configurations in applications
 * @author thoaidc
 */
@Configuration
public class StaticResourcesResolver implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(StaticResourcesResolver.class);

    /**
     * The {@link StaticResourcesResolver} configures Spring to serve static resources
     * from directories on the classpath (e.g. static, content, i18n)<p>
     * The static resource paths defined in {@link BaseConstants.STATIC_RESOURCES#PATHS}
     * will be mapped to the directories listed in {@link BaseConstants.STATIC_RESOURCES#LOCATIONS} <p>
     * When a request comes in for static resources such as .js, .css, .svg, etc.,
     * Spring will look for the files in the configured directories and return the corresponding content
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.debug("Configured custom resources handler");
        ResourceHandlerRegistration resourceHandler = registry.addResourceHandler(BaseConstants.STATIC_RESOURCES.PATHS);
        resourceHandler.addResourceLocations(BaseConstants.STATIC_RESOURCES.LOCATIONS);
    }
}
