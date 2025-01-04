package com.dct.base.dto;

import org.springframework.security.core.Authentication;

public class BaseAuthTokenDTO {

    private Authentication authentication;
    private String username;
    private Integer ID, deviceID;
    private Boolean isRememberMe;

    public BaseAuthTokenDTO() {}

    public BaseAuthTokenDTO(Authentication authentication, String username) {
        this.authentication = authentication;
        this.username = username;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }

    public Boolean isRememberMe() {
        return isRememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        isRememberMe = rememberMe;
    }
}
