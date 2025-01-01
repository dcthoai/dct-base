package com.dct.base.autoconfig.security.jwt;

import com.dct.base.common.MessageTranslationUtils;
import com.dct.base.core.security.BaseSecurityAuthorizeRequestConfig;
import com.dct.base.core.security.jwt.BaseJwtFilter;
import com.dct.base.core.security.jwt.BaseJwtProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class DefaultBaseJwtFilter extends BaseJwtFilter {

    private static final Logger log = LoggerFactory.getLogger(DefaultBaseJwtFilter.class);
    private final BaseJwtProvider jwtProvider;
    private final BaseSecurityAuthorizeRequestConfig authorizeRequestConfig;

    @Autowired
    public DefaultBaseJwtFilter(MessageTranslationUtils messageTranslationUtils,
                                   BaseJwtProvider jwtProvider,
                                   BaseSecurityAuthorizeRequestConfig authorizeRequestConfig) {
        super(messageTranslationUtils);
        this.jwtProvider = jwtProvider;
        this.authorizeRequestConfig = authorizeRequestConfig;
        log.debug("`DefaultBaseJwtFilter` has been automatically configured to validate request token");
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
