package com.dct.base.security.config;

import com.dct.base.config.properties.GoogleOAuth2Properties;
import com.dct.base.constants.PropertiesConstants;
import com.dct.base.constants.SecurityConstants;
import com.dct.base.security.exception.OAuth2AuthenticationFailureHandler;
import com.dct.base.security.exception.OAuth2AuthenticationSuccessHandler;
import com.dct.base.security.service.CustomOAuth2AuthorizationRequestResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@ConditionalOnProperty(name = PropertiesConstants.OAUTH2_ACTIVE_STATUS, havingValue = "true")
public class OAuth2ClientConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ClientConfig.class);
    private final GoogleOAuth2Properties googleOAuth2Properties;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    public OAuth2ClientConfig(@Qualifier("googleOAuth2Properties") GoogleOAuth2Properties googleOAuth2Properties,
                              OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                              OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.googleOAuth2Properties = googleOAuth2Properties;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.oauth2Login(oAuth2Config -> oAuth2Config
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)
            .authorizationEndpoint(config -> config.authorizationRequestResolver(customOAuth2AuthorizationRequestResolver()))
        );
    }

    @Bean
    public CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver() {
        return new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository(), googleOAuth2Properties);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        log.debug("Configuring OAuth2Client with Google info");

        // Create additional ClientRegistration and use the code below if you want to register using multiple Oauth2 providers
        // return new InMemoryClientRegistrationRepository(googleClientRegistration(), facebookClientRegistration());

        return new InMemoryClientRegistrationRepository(googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId(SecurityConstants.OAUTH2_PROVIDER.GOOGLE)
                .clientId(googleOAuth2Properties.getClientID())
                .clientName(googleOAuth2Properties.getClientName())
                .clientSecret(googleOAuth2Properties.getClientSecret())
                .scope(googleOAuth2Properties.getScope())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri(googleOAuth2Properties.getAuthorizationUri())
                .redirectUri(googleOAuth2Properties.getRedirectUri())
                .tokenUri(googleOAuth2Properties.getTokenUri())
                .userInfoUri(googleOAuth2Properties.getUserInfoUri())
                .userNameAttributeName(googleOAuth2Properties.getUsernameAttributeName())
                .build();
    }
}
