package com.dct.base.security.config;

import com.dct.base.constants.SecurityConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

public interface SecurityAuthorizeHttpRequest {

    String[] getPublicPatterns();

    String[] getPrivatePatterns();

    @Component
    @ConditionalOnMissingBean(SecurityAuthorizeHttpRequest.class)
    class DefaultSecurityAuthorizeHttpRequest implements SecurityAuthorizeHttpRequest {

        @Override
        public String[] getPublicPatterns() {
            return SecurityConstants.REQUEST_MATCHERS.DEFAULT_PUBLIC_API_PATTERNS;
        }

        @Override
        public String[] getPrivatePatterns() {
            return new String[]{};
        }
    }
}
