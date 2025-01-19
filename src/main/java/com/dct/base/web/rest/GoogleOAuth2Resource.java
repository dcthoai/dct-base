package com.dct.base.web.rest;

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
@SuppressWarnings("unused")
public class GoogleOAuth2Resource {

    private static final Logger log = LoggerFactory.getLogger(GoogleOAuth2Resource.class);

    @GetMapping("/google/authenticate")
    public void handleGoogleCallback(@RequestParam(value = "state", required = false) String state,
                                    @RequestParam(value = "code", required = false) String code,
                                    HttpServletResponse response) throws IOException {
        log.debug("REST request callback from google authenticate. GET: /api/p/callback/oauth2/google/authenticate");
        log.debug("State after authorization: {}", state);
    }
}
