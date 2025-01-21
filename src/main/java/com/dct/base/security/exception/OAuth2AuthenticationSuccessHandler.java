package com.dct.base.security.exception;

import com.dct.base.constants.PropertiesConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.security.model.OAuth2UserInfoResponse;
import com.dct.base.service.GoogleAuthenticateService;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(name = PropertiesConstants.OAUTH2_ACTIVE_STATUS, havingValue = "true")
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);
    private final GoogleAuthenticateService googleAuthenticateService;
    private final ObjectMapper objectMapper;

    public OAuth2AuthenticationSuccessHandler(@Lazy GoogleAuthenticateService googleAuthenticateService,
                                              ObjectMapper objectMapper) {
        this.googleAuthenticateService = googleAuthenticateService;
        this.objectMapper = objectMapper;
        log.debug("Configured 'OAuth2AuthenticationSuccessHandler' for use");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        log.debug("OAuth2AuthenticationSuccessHandler is active");
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> userInfo = authToken.getPrincipal().getAttributes();
        OAuth2UserInfoResponse userInfoResponse = objectMapper.convertValue(userInfo, OAuth2UserInfoResponse.class);
        BaseResponseDTO responseDTO = googleAuthenticateService.authorize(userInfoResponse);
        Cookie tokenCookie = (Cookie) responseDTO.getResult();

        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(false); // Set true for HTTPS protocol only
        tokenCookie.setPath("/");

        response.addCookie(tokenCookie);
        log.debug("Set token in secure cookie successful");
    }
}
