package com.dct.base.security;

import com.dct.base.common.BaseCommon;
import com.dct.base.common.JsonUtils;
import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final BaseCommon baseCommon;

    public CustomAuthenticationEntryPoint(BaseCommon baseCommon) {
        this.baseCommon = baseCommon;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Authentication entry point is active. {}: {}", authException.getMessage(), request.getRequestURL());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatusConstants.UNAUTHORIZED);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.UNAUTHORIZED,
            HttpStatusConstants.STATUS.FAILED,
            baseCommon.getMessageI18n(ExceptionConstants.UNAUTHORIZED)
        );

        response.getWriter().write(JsonUtils.toJsonString(responseDTO));
        response.flushBuffer();
    }
}
