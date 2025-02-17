package com.dct.base.web.rest;

import com.dct.base.constants.SecurityConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.security.service.CustomOAuth2AuthorizationRequestResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class GoogleOAuth2Resource {

    private static final Logger log = LoggerFactory.getLogger(GoogleOAuth2Resource.class);
    private final CustomOAuth2AuthorizationRequestResolver authorizationRequestResolver;

    public GoogleOAuth2Resource(CustomOAuth2AuthorizationRequestResolver authorizationRequestResolver) {
        this.authorizationRequestResolver = authorizationRequestResolver;
    }

    @GetMapping("/p/common/auth/oauth2/google")
    @ResponseBody
    public BaseResponseDTO getGoogleAuthUrl(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = authorizationRequestResolver.resolve(
            request,
            SecurityConstants.OAUTH2_PROVIDER.GOOGLE
        );

        if (authorizationRequest == null) {
            throw new RuntimeException("Authorization request not found");
        }

        System.out.println(authorizationRequest.getState());

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
        String redirectUrl = "/login/oauth2/code/google?" + request.getQueryString();
        response.sendRedirect(redirectUrl);
    }
}