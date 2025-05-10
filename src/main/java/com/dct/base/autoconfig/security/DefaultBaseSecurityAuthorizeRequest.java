package com.dct.base.autoconfig.security;

import com.dct.base.core.security.BaseSecurityAuthorizeRequestConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(name = "customSecurityAuthorizeRequest")
public class DefaultBaseSecurityAuthorizeRequest extends BaseSecurityAuthorizeRequestConfig {}
