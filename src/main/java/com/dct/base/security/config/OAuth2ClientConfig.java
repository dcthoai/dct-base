package com.dct.base.security.config;

import com.dct.base.config.properties.OAuth2Properties;
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
    private final OAuth2Properties oAuth2Properties;

    public OAuth2ClientConfig(@Qualifier("OAuth2Properties") OAuth2Properties oAuth2Properties) {
        this.oAuth2Properties = oAuth2Properties;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        log.debug("Configuring OAuth2Client with Google info");
        return new InMemoryClientRegistrationRepository(googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId(oAuth2Properties.getGoogleClientID())
                .clientName(oAuth2Properties.getClientName())
                .clientSecret(oAuth2Properties.getGoogleClientSecret())
                .scope(oAuth2Properties.getScope())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri(oAuth2Properties.getAuthorizationUri())
                .redirectUri(oAuth2Properties.getGoogleAuthRedirectURL())
                .tokenUri(oAuth2Properties.getTokenUri())
                .userInfoUri(oAuth2Properties.getUserInfoUri())
                .userNameAttributeName(oAuth2Properties.getUsernameAttributeName())
                .build();
    }
}
