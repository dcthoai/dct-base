package com.dct.base.security.config;

import com.dct.base.constants.BaseSecurityConstants;
import org.springframework.stereotype.Component;

@Component
public class DefaultSecurityAuthorizeHttpRequest implements SecurityAuthorizeHttpRequest {

    @Override
    public String[] getPublicPatterns() {
        return BaseSecurityConstants.REQUEST_MATCHERS.DEFAULT_PUBLIC_API_PATTERNS;
    }

    @Override
    public String[] getPrivatePatterns() {
        return new String[0];
    }
}
