package com.dct.base.security.config;

public interface SecurityAuthorizeHttpRequest {
    String[] getPublicPatterns();
    String[] getPrivatePatterns();
}
