package com.dct.base.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "dct-base.security.oauth2")
public class OAuth2Properties {

    private String clientName;
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private String usernameAttributeName;
    private String googleClientID;
    private String googleClientSecret;
    private String googleAuthRedirectURL;
    private List<String> scope;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public String getUsernameAttributeName() {
        return usernameAttributeName;
    }

    public void setUsernameAttributeName(String usernameAttributeName) {
        this.usernameAttributeName = usernameAttributeName;
    }

    public String getGoogleClientID() {
        return googleClientID;
    }

    public void setGoogleClientID(String googleClientID) {
        this.googleClientID = googleClientID;
    }

    public String getGoogleClientSecret() {
        return googleClientSecret;
    }

    public void setGoogleClientSecret(String googleClientSecret) {
        this.googleClientSecret = googleClientSecret;
    }

    public String getGoogleAuthRedirectURL() {
        return googleAuthRedirectURL;
    }

    public void setGoogleAuthRedirectURL(String googleAuthRedirectURL) {
        this.googleAuthRedirectURL = googleAuthRedirectURL;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }
}
