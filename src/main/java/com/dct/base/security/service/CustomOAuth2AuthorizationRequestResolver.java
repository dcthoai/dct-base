package com.dct.base.security.service;

import com.dct.base.config.properties.GoogleOAuth2Properties;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2AuthorizationRequestResolver.class);
    private final DefaultOAuth2AuthorizationRequestResolver delegate;
    private final GoogleOAuth2Properties googleOAuth2Properties;

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository client,
                                                    GoogleOAuth2Properties properties) {
        this.googleOAuth2Properties = properties;
        this.delegate = new DefaultOAuth2AuthorizationRequestResolver(client, properties.getBaseAuthorizeUri());
        log.debug("'CustomOAuth2AuthorizationRequestResolver' is configured for use");
        log.debug("Use URI: {} for authenticate via OAuth2 provider", properties.getBaseAuthorizeUri());
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        if (!request.getRequestURI().startsWith(googleOAuth2Properties.getBaseAuthorizeUri()))
            return null;

        log.debug("Authenticating via default OAuth2 provider from: {}", request.getRequestURI());
        OAuth2AuthorizationRequest authorizationRequest = delegate.resolve(request);

        return requestAdditionalRefreshToken(authorizationRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        if (!request.getRequestURI().startsWith(googleOAuth2Properties.getBaseAuthorizeUri()))
            return null;

        log.debug("Authenticating via {} from: {}", clientRegistrationId, request.getRequestURI());
        OAuth2AuthorizationRequest authorizationRequest = delegate.resolve(request, clientRegistrationId);

        return requestAdditionalRefreshToken(authorizationRequest);
    }

    private OAuth2AuthorizationRequest requestAdditionalRefreshToken(OAuth2AuthorizationRequest authorizationRequest) {
        if (Objects.isNull(authorizationRequest))
            return null;

        log.debug("Modifying request, require issue additional refresh token after successful authentication");

        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(params -> params.put("access_type", "offline"))
                .build();
    }
}
