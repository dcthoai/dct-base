package com.dct.base.security.config;

import com.dct.base.config.properties.GoogleOAuth2Properties;
import com.dct.base.constants.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
public class OAuth2ClientConfig {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ClientConfig.class);
    private final GoogleOAuth2Properties googleOAuth2Properties;

    public OAuth2ClientConfig(@Qualifier("googleOAuth2Properties") GoogleOAuth2Properties googleOAuth2Properties) {
        this.googleOAuth2Properties = googleOAuth2Properties;
    }

    @Bean
    protected ClientRegistrationRepository clientRegistrationRepository() {
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
