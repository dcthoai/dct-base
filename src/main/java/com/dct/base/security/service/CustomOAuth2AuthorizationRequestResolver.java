package com.dct.base.security.service;

import com.dct.base.config.properties.GoogleOAuth2Properties;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2AuthorizationRequestResolver.class);
    private final DefaultOAuth2AuthorizationRequestResolver delegate;

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository client,
                                                    @Qualifier("googleOAuth2Properties") GoogleOAuth2Properties properties) {
        this.delegate = new DefaultOAuth2AuthorizationRequestResolver(client, properties.getBaseAuthorizeUri());
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = delegate.resolve(request);

        if (authorizationRequest != null) {
            log.debug("Custom resolver: Modifying authorization request for URI: {}", request.getRequestURI());
            return customizeAuthorizationRequest(authorizationRequest);
        }

        return null;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = delegate.resolve(request, clientRegistrationId);

        if (authorizationRequest != null) {
            log.debug("Custom resolver: Modifying authorization request for client ID: {}", clientRegistrationId);
            return customizeAuthorizationRequest(authorizationRequest);
        }

        return null;
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest) {
        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(params -> params.put("access_type", "offline"))
                .build();
    }
}
