package com.dct.base.autoconfig.security.jwt;

import com.dct.base.config.properties.BaseSecurityProperties;
import com.dct.base.core.security.jwt.BaseJwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DefaultBaseJwtProvider extends BaseJwtProvider {

    @Autowired
    public DefaultBaseJwtProvider(@Qualifier("baseSecurityProperties") BaseSecurityProperties securityProperties) {
        super(securityProperties);
    }
}
