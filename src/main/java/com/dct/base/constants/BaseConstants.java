package com.dct.base.constants;

public interface BaseConstants {

    String DEFAULT_CREATOR = "system";
    String MESSAGE_SOURCE_BASENAME = "classpath:i18n/messages";
    String MESSAGE_SOURCE_ENCODING = "UTF-8";

    String[] INTERCEPTOR_EXCLUDED_PATHS = {
        "**/favicon.ico",
        "/images/**",
        "**images**",
        "/index.html",
        "**index.html**",
        "**/file/**",
        "**/login/**",
        "/error**",
        "/i18n/**"
    };

    interface STATIC_RESOURCES {

        String[] PATHS = {
            "/*.js",
            "/*.css",
            "/*.svg",
            "/*.png",
            "/*.ico",
            "/content/**",
            "/i18n/*",
        };

        String[] LOCATIONS = {
            "classpath:/static/",
            "classpath:/static/content/",
            "classpath:/static/i18n/",
        };
    }
}
