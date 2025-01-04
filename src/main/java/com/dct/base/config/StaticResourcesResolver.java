package com.dct.base.config;

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

    protected static final String[] RESOURCE_LOCATIONS = new String[] {
        "classpath:/static/",
        "classpath:/static/content/",
        "classpath:/static/i18n/",
    };

    protected static final String[] RESOURCE_PATHS = new String[] {
        "/*.js",
        "/*.css",
        "/*.svg",
        "/*.png",
        "/*.ico",
        "/content/**",
        "/i18n/*",
    };

    /**
     * The {@link StaticResourcesResolver} configures Spring to serve static resources
     * from directories on the classpath (e.g. static, content, i18n)<p>
     * The static resource paths defined in <a href="">RESOURCE_PATHS</a>
     * will be mapped to the directories listed in <a href="">RESOURCE_LOCATIONS</a><p>
     * When a request comes in for static resources such as .js, .css, .svg, etc.,
     * Spring will look for the files in the configured directories and return the corresponding content.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler(RESOURCE_PATHS);
        resourceHandlerRegistration.addResourceLocations(RESOURCE_LOCATIONS);
    }
}
