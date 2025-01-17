package com.dct.base.dto;

import org.springframework.security.core.Authentication;

public class BaseAuthTokenDTO {

    private Authentication authentication;
    private String username;
    private String deviceID = "";
    private Integer userID;
    private Boolean isRememberMe = false;

    public BaseAuthTokenDTO() {}

    public BaseAuthTokenDTO(Authentication authentication, String username) {
        this.authentication = authentication;
        this.username = username;
    }

    public BaseAuthTokenDTO(Authentication authentication, String username, Integer userID) {
        this.authentication = authentication;
        this.username = username;
        this.userID = userID;
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

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Boolean isRememberMe() {
        return isRememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        isRememberMe = rememberMe;
    }
}
