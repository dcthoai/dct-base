package com.dct.base.security.handler;

import com.dct.base.constants.PropertiesConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.security.model.OAuth2UserInfoResponse;
import com.dct.base.service.GoogleAuthenticationService;

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

import java.io.IOException;
import java.util.Map;

/**
 * Handle the logic after successful OAuth2 authentication <p>
 * {@link PropertiesConstants#OAUTH2_ACTIVE_STATUS} helps turn on/off the OAuth2 feature based on the configuration <p>
 * {@link ConditionalOnProperty} mark this class to be initialized only if the property OAUTH2_ACTIVE_STATUS is "true"
 *
 * @author thoaidc
 */
@Component
@ConditionalOnProperty(name = PropertiesConstants.OAUTH2_ACTIVE_STATUS, havingValue = "true")
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);
    private final GoogleAuthenticationService googleAuthenticationService;
    private final ObjectMapper objectMapper;

    public OAuth2AuthenticationSuccessHandler(@Lazy GoogleAuthenticationService googleAuthenticationService,
                                              ObjectMapper objectMapper) {
        this.googleAuthenticationService = googleAuthenticationService;
        this.objectMapper = objectMapper;
        log.debug("Configured 'OAuth2AuthenticationSuccessHandler' for use");
    }

    /**
     * Customize the business logic when OAuth2 authentication is successful here <p>
     * For example: create an access token, create default account information, etc <p>
     * In this case, we create default account information for the user from the data provided by Google,
     * along with the access_token stored in an HTTP-only cookie
     *
     * @param request the request which caused the successful authentication
     * @param response the response
     * @param authentication the <tt>Authentication</tt> object which was created during the authentication process
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.debug("OAuth2AuthenticationSuccessHandler is active");
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;

        // Get user profile from Google response
        Map<String, Object> userInfo = authToken.getPrincipal().getAttributes();
        OAuth2UserInfoResponse userInfoResponse = objectMapper.convertValue(userInfo, OAuth2UserInfoResponse.class);

        // Check and create default account information for the user and create an access token stored in cookies
        BaseResponseDTO responseDTO = googleAuthenticationService.authorize(userInfoResponse);
        Cookie tokenCookie = (Cookie) responseDTO.getResult();

        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(false); // Set true for HTTPS protocol only
        tokenCookie.setPath("/");

        response.addCookie(tokenCookie);
        log.debug("Set token in secure cookie successful");

        response.setHeader("Content-Type", "text/html");
        response.getWriter().write("<script>window.opener.postMessage('auth-success', '*'); window.close();</script>");
        response.flushBuffer();
    }
}
