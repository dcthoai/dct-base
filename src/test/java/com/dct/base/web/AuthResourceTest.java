package com.dct.base.web;

import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.security.service.GoogleOAuth2Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AuthResourceTest {

    private final GoogleOAuth2Service googleOAuth2Service;

    public AuthResourceTest(GoogleOAuth2Service googleOAuth2Service) {
        this.googleOAuth2Service = googleOAuth2Service;
    }

    @PostMapping("/google/auth-code")
    public BaseResponseDTO getAuthorizationCode() {
        return new BaseResponseDTO(googleOAuth2Service.getAuthorizationCode());
    }
}
