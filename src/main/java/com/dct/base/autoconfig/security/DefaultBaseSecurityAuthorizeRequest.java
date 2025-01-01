package com.dct.base.autoconfig.security;

import com.dct.base.core.security.BaseSecurityAuthorizeRequestConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultBaseSecurityAuthorizeRequest extends BaseSecurityAuthorizeRequestConfig {

    private static final Logger log = LoggerFactory.getLogger(DefaultBaseSecurityAuthorizeRequest.class);

    public DefaultBaseSecurityAuthorizeRequest() {
        log.debug("`DefaultBaseSecurityAuthorizeRequest` has been automatically configured");
    }
}
