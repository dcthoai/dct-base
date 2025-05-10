package com.dct.base.autoconfig.security;

import com.dct.base.core.security.BaseSecurityConfigAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(value = BaseSecurityConfigAdapter.class)
public class DefaultBaseSecurityConfig extends BaseSecurityConfigAdapter {

    private static final Logger log = LoggerFactory.getLogger(DefaultBaseSecurityConfig.class);

    public DefaultBaseSecurityConfig() {
        log.debug("`DefaultBaseSecurityConfig` has been automatically configured");
    }
}
