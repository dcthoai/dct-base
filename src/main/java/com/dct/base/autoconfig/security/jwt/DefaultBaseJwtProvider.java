package com.dct.base.autoconfig.security.jwt;

import com.dct.base.config.properties.BaseSecurityProperties;
import com.dct.base.core.security.jwt.BaseJwtProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DefaultBaseJwtProvider extends BaseJwtProvider {

    private static final Logger log = LoggerFactory.getLogger(DefaultBaseJwtProvider.class);
    @Autowired
    public DefaultBaseJwtProvider(@Qualifier("baseSecurityProperties") BaseSecurityProperties securityProperties) {
        super(securityProperties);
        log.debug("`DefaultBaseJwtProvider` has been automatically configured to handle jwt");
    }
}
