package com.dct.base.security.config;

import com.dct.base.constants.SecurityConstants;
import org.springframework.stereotype.Component;

import java.util.List;

public interface SecurityPathConfig {

    List<String> getPublicPaths();

    @Component
    class DefaultSecurityPathConfig implements SecurityPathConfig {

        @Override
        public List<String> getPublicPaths() {
            return List.of(SecurityConstants.REQUEST_MATCHERS.DEFAULT_API_PUBLIC_PATTERN);
        }
    }
}
