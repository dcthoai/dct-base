package com.dct.base.constants;

@SuppressWarnings("unused")
public interface BaseConstants {

    String DEFAULT_CREATOR = "system";
    String MESSAGE_SOURCE_ENCODING = "UTF-8";

    String[] MESSAGE_SOURCE_BASENAME = {
        "classpath:common/i18n/messages",
        "classpath:i18n/messages"
    };

    String[] INTERCEPTOR_EXCLUDED_PATHS = {
        "**/favicon.ico",
        "/images/**",
        "**images**",
        "/index.html",
        "**index.html**",
        "**/file/**",
        "**/login/**",
        "/error**",
        "/i18n/**",
        "/common/i18n/**"
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
            "/common/i18n/*"
        };

        String[] LOCATIONS = {
            "classpath:/static/",
            "classpath:/static/content/",
            "classpath:/static/i18n/",
            "classpath:/static/common/i18n/",
        };
    }

    interface REGEX {
        String USERNAME_PATTERN = "^[a-zA-Z0-9]{2,45}$";
        String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{8,20}$";
        String EMAIL_PATTERN = "^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        String PHONE_PATTERN = "^\\+?(\\d{1,3})(\\s|\\.|-)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9])|([1-9]\\d{1,2}))(\\s|\\.|-)?\\d{3}(\\s|\\.|-)?\\d{3,4}$\n";
    }
}
