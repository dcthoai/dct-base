package com.dct.base.autoconfig;

import com.dct.base.autoconfig.exception.DefaultBaseExceptionHandler;
import com.dct.base.autoconfig.interceptor.DefaultBaseResponseFilter;
import com.dct.base.autoconfig.security.DefaultBaseSecurityAuthorizeRequest;
import com.dct.base.autoconfig.security.DefaultBaseSecurityConfig;
import com.dct.base.autoconfig.security.handler.DefaultBaseAccessDeniedHandler;
import com.dct.base.autoconfig.security.handler.DefaultBaseAuthenticationEntryPoint;
import com.dct.base.autoconfig.security.jwt.DefaultBaseJwtFilter;
import com.dct.base.autoconfig.security.jwt.DefaultBaseJwtProvider;
import com.dct.base.common.MessageTranslationUtils;
import com.dct.base.config.properties.BaseSecurityProperties;
import com.dct.base.core.exception.BaseExceptionHandler;
import com.dct.base.core.interceptor.BaseResponseFilter;
import com.dct.base.core.security.BaseSecurityAuthorizeRequestConfig;
import com.dct.base.core.security.BaseSecurityConfigAdapter;
import com.dct.base.core.security.handler.BaseAccessDeniedHandler;
import com.dct.base.core.security.handler.BaseAuthenticationEntryPoint;
import com.dct.base.core.security.jwt.BaseJwtFilter;
import com.dct.base.core.security.jwt.BaseJwtProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfigBeanAdapter {

    @Bean
    @ConditionalOnMissingBean(BaseExceptionHandler.class)
    public BaseExceptionHandler defaultExceptionHandler(MessageTranslationUtils messageTranslationUtils) {
        return new DefaultBaseExceptionHandler(messageTranslationUtils);
    }

    @Bean
    @ConditionalOnMissingBean(BaseResponseFilter.class)
    public BaseResponseFilter defaultResponseFilter(MessageTranslationUtils messageTranslationUtils) {
        return new DefaultBaseResponseFilter(messageTranslationUtils);
    }

    @Bean
    @ConditionalOnMissingBean(BaseAccessDeniedHandler.class)
    public BaseAccessDeniedHandler defaultAccessDeniedHandler(MessageTranslationUtils messageTranslationUtils) {
        return new DefaultBaseAccessDeniedHandler(messageTranslationUtils);
    }

    @Bean
    @ConditionalOnMissingBean(BaseAuthenticationEntryPoint.class)
    public BaseAuthenticationEntryPoint defaultAuthenticationEntryPoint(MessageTranslationUtils messageUtils) {
        return new DefaultBaseAuthenticationEntryPoint(messageUtils);
    }

    @Bean
    @ConditionalOnMissingBean(BaseJwtProvider.class)
    public BaseJwtProvider defaultJwtProvider(BaseSecurityProperties securityProperties) {
        return new DefaultBaseJwtProvider(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(BaseSecurityAuthorizeRequestConfig.class)
    public BaseSecurityAuthorizeRequestConfig defaultBaseSecurityAuthorizeRequestConfig() {
        return new DefaultBaseSecurityAuthorizeRequest();
    }

    @Bean
    @ConditionalOnMissingBean(BaseJwtFilter.class)
    public BaseJwtFilter defaultJwtFilter(MessageTranslationUtils messageUtils,
                                          BaseJwtProvider jwtProvider,
                                          BaseSecurityAuthorizeRequestConfig authorizeRequestConfig) {
        return new DefaultBaseJwtFilter(messageUtils, jwtProvider, authorizeRequestConfig);
    }

    @Bean
    @ConditionalOnMissingBean(BaseSecurityConfigAdapter.class)
    public BaseSecurityConfigAdapter defaultSecurityConfigAdapter() {
        return new DefaultBaseSecurityConfig();
    }
}
