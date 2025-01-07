package com.dct.base.security;

import com.dct.base.common.JsonUtils;
import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.debug("Authentication entry point is active", authException);

        response.setContentType("application/json");
        response.setStatus(HttpStatusConstants.UNAUTHORIZED);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.UNAUTHORIZED,
            HttpStatusConstants.STATUS.FAILED,
            ExceptionConstants.UNAUTHORIZED
        );

        response.getWriter().write(JsonUtils.toJsonString(responseDTO));
    }
}
