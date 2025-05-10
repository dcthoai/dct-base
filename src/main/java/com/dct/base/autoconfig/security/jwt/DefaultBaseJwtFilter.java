package com.dct.base.autoconfig.security.jwt;

import com.dct.base.common.MessageTranslationUtils;
import com.dct.base.core.security.BaseSecurityAuthorizeRequestConfig;
import com.dct.base.core.security.jwt.BaseJwtFilter;
import com.dct.base.core.security.jwt.BaseJwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class DefaultBaseJwtFilter extends BaseJwtFilter {

    private final BaseJwtProvider jwtProvider;
    private final BaseSecurityAuthorizeRequestConfig authorizeRequestConfig;

    @Autowired
    protected DefaultBaseJwtFilter(MessageTranslationUtils messageTranslationUtils,
                                   BaseJwtProvider jwtProvider,
                                   BaseSecurityAuthorizeRequestConfig authorizeRequestConfig) {
        super(messageTranslationUtils);
        this.jwtProvider = jwtProvider;
        this.authorizeRequestConfig = authorizeRequestConfig;
    }

    @Override
    protected Authentication validateToken(String token) {
        return jwtProvider.validateToken(token);
    }

    @Override
    protected String[] publicRequestPatterns() {
        return authorizeRequestConfig.getPublicPatterns();
    }
}
