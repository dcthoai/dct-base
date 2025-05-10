package com.dct.base.core.security;

import com.dct.base.constants.BaseSecurityConstants;

public abstract class BaseSecurityAuthorizeRequestConfig {

    public String[] getPublicPatterns() {
        return BaseSecurityConstants.REQUEST_MATCHERS.DEFAULT_PUBLIC_API_PATTERNS;
    }

    public String[] getPrivatePatterns() {
        return new String[0];
    }
}
