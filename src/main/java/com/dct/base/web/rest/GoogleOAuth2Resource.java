package com.dct.base.web.rest;

import com.dct.base.config.properties.GoogleOAuth2Config;
import com.dct.base.config.properties.OAuth2Config;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.security.service.CustomOAuth2AuthorizationRequestResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class GoogleOAuth2Resource {

    private static final Logger log = LoggerFactory.getLogger(GoogleOAuth2Resource.class);
    private final CustomOAuth2AuthorizationRequestResolver authorizationRequestResolver;
    private final OAuth2Config oAuth2Configs;
    private final GoogleOAuth2Config googleOAuth2Config;

    public GoogleOAuth2Resource(CustomOAuth2AuthorizationRequestResolver authorizationRequestResolver,
                                @Qualifier("OAuth2Config") OAuth2Config oAuth2Configs,
                                @Qualifier("googleOAuth2Config") GoogleOAuth2Config googleOAuth2Config) {
        this.authorizationRequestResolver = authorizationRequestResolver;
        this.oAuth2Configs = oAuth2Configs;
        this.googleOAuth2Config = googleOAuth2Config;
    }

    @GetMapping("/p/common/auth/oauth2/authorize/url/google")
    public BaseResponseDTO getGoogleAuthUrl(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = authorizationRequestResolver.resolve(
            request,
            googleOAuth2Config.getClientRegistrationId()
        );

        if (authorizationRequest == null) {
            throw new RuntimeException("Authorization request not found");
        }

        String sessionOAuth2AuthorizationRequestName = HttpSessionOAuth2AuthorizationRequestRepository.class.getName();
        String sessionAttributeName = sessionOAuth2AuthorizationRequestName + ".AUTHORIZATION_REQUEST";
        request.getSession().setAttribute(sessionAttributeName, authorizationRequest);

        return new BaseResponseDTO(authorizationRequest.getAuthorizationRequestUri());
    }

    @GetMapping("/p/common/callback/auth/oauth2/code/google")
    public void googleAuthorizationCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Google OAuth2 callback received");
        String state = request.getParameter("state");

        // Check state to prevent CSRF
        if (Objects.isNull(state)) {
            log.error("Invalid state parameter detected!");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid state parameter");
            return;
        }

        // Redirect to Spring Security default URL, keeping all query params intact
        String redirectUrl = oAuth2Configs.getDefaultRedirectUri() + googleOAuth2Config.getClientRegistrationId();
        response.sendRedirect(redirectUrl + '?' + request.getQueryString());
    }
}