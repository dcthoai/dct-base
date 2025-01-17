package com.dct.base.security.service;

import com.dct.base.common.JsonUtils;
import com.dct.base.config.properties.OAuth2Properties;
import com.dct.base.security.model.OAuth2TokenResponse;
import com.dct.base.security.model.OAuth2UserInfoResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleOAuth2Service {

    private static final Logger log = LoggerFactory.getLogger(GoogleOAuth2Service.class);
    private final OAuth2Properties oAuth2Config;
    private final RestTemplate restTemplate;
    private String authorizationCode;

    public GoogleOAuth2Service(@Qualifier("OAuth2Properties") OAuth2Properties oAuth2Config,
                               RestTemplate restTemplate) {
        this.oAuth2Config = oAuth2Config;
        this.restTemplate = restTemplate;
    }

    private MultiValueMap<String, String> getGoogleOAuth2Config(String authorizationCode) {
        MultiValueMap<String, String> config = new LinkedMultiValueMap<>();

        config.add("code", authorizationCode);
        config.add("client_id", oAuth2Config.getGoogleClientID());
        config.add("client_secret", oAuth2Config.getGoogleClientSecret());
        config.add("redirect_uri", oAuth2Config.getGoogleAuthRedirectURL());
        config.add("grant_type", AuthorizationGrantType.AUTHORIZATION_CODE.getValue());

        return config;
    }

    public OAuth2TokenResponse getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(getGoogleOAuth2Config(code), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(oAuth2Config.getTokenUri(), request, String.class);
        return JsonUtils.parseJson(response.getBody(), OAuth2TokenResponse.class);
    }

    public OAuth2UserInfoResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            oAuth2Config.getUserInfoUri(),
            HttpMethod.GET,
            request,
            String.class
        );

        return JsonUtils.parseJson(response.getBody(), OAuth2UserInfoResponse.class);
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
}
