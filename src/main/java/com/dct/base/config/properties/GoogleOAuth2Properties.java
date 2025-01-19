package com.dct.base.config.properties;

import com.dct.base.constants.PropertiesConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = PropertiesConstants.GOOGLE_OAUTH2_PROPERTIES)
public class GoogleOAuth2Properties {

    private String clientID;
    private String clientName;
    private String clientSecret;
    private String baseAuthorizeUri;
    private String authorizationUri;
    private String redirectUri;
    private String tokenUri;
    private String userInfoUri;
    private String usernameAttributeName;
    private List<String> scope;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getBaseAuthorizeUri() {
        return baseAuthorizeUri;
    }

    public void setBaseAuthorizeUri(String baseAuthorizeUri) {
        this.baseAuthorizeUri = baseAuthorizeUri;
    }

    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
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

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }
}
