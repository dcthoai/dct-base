package com.dct.base.service;

import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.security.model.OAuth2UserInfoResponse;

@SuppressWarnings("unused")
public interface GoogleAuthenticateService {

    BaseResponseDTO authorize(String code);
    BaseResponseDTO authorize(OAuth2UserInfoResponse userInfo);
}
