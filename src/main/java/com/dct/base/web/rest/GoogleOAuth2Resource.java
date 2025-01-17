package com.dct.base.web.rest;

import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/p/callback/oauth2")
public class GoogleOAuth2Resource {

    private static final Logger log = LoggerFactory.getLogger(GoogleOAuth2Resource.class);
    private final AuthService authService;

    public GoogleOAuth2Resource(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/google/authenticate")
    public BaseResponseDTO handleGoogleCallback(@RequestParam(value = "state", required = false) String state,
                                                @RequestParam(value = "code", required = false) String code,
                                                HttpServletResponse response) throws IOException {
        log.debug("REST request callback from google authenticate. GET: /api/p/callback/oauth2/google/authenticate");
        log.debug("State after authorization: {}", state);
//        response.sendRedirect("/");
        return authService.authenticateFromGoogle(code);
    }
}
