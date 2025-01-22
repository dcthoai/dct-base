package com.dct.base.dto;

import com.dct.base.entity.Account;
import org.springframework.security.core.Authentication;
import com.dct.base.security.jwt.JwtTokenProvider;

/**
 * User information after successful authentication, used to generate the access token
 * Used in {@link JwtTokenProvider#createToken(BaseAuthTokenDTO)}
 *
 * @author thoaidc
 */
@SuppressWarnings("unused")
public class BaseAuthTokenDTO {

    private Authentication authentication; // Contains user authorities information
    private String username;
    private String deviceID = "";
    private Integer userID;
    private Boolean isRememberMe = false;

    public BaseAuthTokenDTO() {}

    public BaseAuthTokenDTO(Authentication authentication, Account account) {
        this.authentication = authentication;
        this.username = account.getUsername();
        this.userID = account.getId();
    }

    public BaseAuthTokenDTO(Authentication authentication, Account account, String deviceID, boolean isRememberMe) {
        this(authentication, account);
        this.deviceID = deviceID;
        this.isRememberMe = isRememberMe;
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
