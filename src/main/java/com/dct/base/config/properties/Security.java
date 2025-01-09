package com.dct.base.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dct-base.security.auth")
public class Security {
    private String base64SecretKey;
    private Long tokenValidity;
    private Long tokenValidityForRememberMe;

    public String getBase64SecretKey() {
        return base64SecretKey;
    }

    public void setBase64SecretKey(String base64SecretKey) {
        this.base64SecretKey = base64SecretKey;
    }

    public Long getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(Long tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    public Long getTokenValidityForRememberMe() {
        return tokenValidityForRememberMe;
    }

    public void setTokenValidityForRememberMe(Long tokenValidityForRememberMe) {
        this.tokenValidityForRememberMe = tokenValidityForRememberMe;
    }
}
