package com.dct.base.web.rest;

import com.dct.base.config.CRLFLogConverter;
import com.dct.base.security.model.OAuth2TokenResponse;
import com.dct.base.security.model.OAuth2UserInfoResponse;
import com.dct.base.security.service.GoogleOAuth2Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/p/callback/oauth2")
public class GoogleOAuth2Resource {

    private static final Logger log = LoggerFactory.getLogger(GoogleOAuth2Resource.class);
    private final GoogleOAuth2Service googleOAuth2Service;

    public GoogleOAuth2Resource(GoogleOAuth2Service googleOAuth2Service) {
        this.googleOAuth2Service = googleOAuth2Service;
    }

    @GetMapping("/google/authenticate")
    public void handleGoogleCallback(@RequestParam(value = "state", required = false) String state,
                                     @RequestParam(value = "code", required = false) String code,
                                     @RequestParam(value = "scope", required = false) String scope) {
        log.debug("REST request callback from google authenticate. GET: /api/p/callback/oauth2/google/authenticate");

        googleOAuth2Service.setAuthorizationCode(code);
        OAuth2TokenResponse tokenResponse = googleOAuth2Service.getAccessToken(code);
        OAuth2UserInfoResponse userInfoResponse = googleOAuth2Service.getUserInfo(tokenResponse.getAccessToken());

        log.debug(CRLFLogConverter.CRLF_SAFE_MARKER, "{}", tokenResponse);
        log.debug(CRLFLogConverter.CRLF_SAFE_MARKER, "{}", userInfoResponse);
    }
}
