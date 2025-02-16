package com.dct.base.security.service;

import com.dct.base.config.properties.GoogleOAuth2Properties;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2AuthorizationRequestResolver.class);
    private final DefaultOAuth2AuthorizationRequestResolver delegate;

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository client,
                                                    GoogleOAuth2Properties properties) {
        final String baseAuthorizeUri = properties.getBaseAuthorizeUri();
        this.delegate = new DefaultOAuth2AuthorizationRequestResolver(client, baseAuthorizeUri);
        log.debug("'CustomOAuth2AuthorizationRequestResolver' is configured for use");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        log.debug("Authenticating via OAuth2 provider from: {}", request.getRequestURI());
        OAuth2AuthorizationRequest authorizationRequest = delegate.resolve(request);

        if (authorizationRequest != null) {
            log.debug("Modifying authorization request: {}", request.getRequestURI());
            return requestAdditionalRefreshToken(authorizationRequest);
        }

        return null;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        log.debug("Authenticating via {}", clientRegistrationId);
        OAuth2AuthorizationRequest authorizationRequest = delegate.resolve(request, clientRegistrationId);

        if (authorizationRequest != null) {
            log.debug("Modifying authorization request: {}/{}", request.getRequestURI(), clientRegistrationId);
            return requestAdditionalRefreshToken(authorizationRequest);
        }

        return null;
    }

    private OAuth2AuthorizationRequest requestAdditionalRefreshToken(OAuth2AuthorizationRequest authorizationRequest) {
        log.debug("Request Google to issue additional refresh token after successful authentication");

        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(params -> params.put("access_type", "offline"))
                .build();
    }
}
