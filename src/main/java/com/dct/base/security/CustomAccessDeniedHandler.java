package com.dct.base.security;

import com.dct.base.common.JsonUtils;
import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.debug("AccessDenied handler is active", accessDeniedException);

        response.setContentType("application/json");
        response.setStatus(HttpStatusConstants.FORBIDDEN);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.FORBIDDEN,
            HttpStatusConstants.STATUS.FAILED,
            ExceptionConstants.FORBIDDEN
        );

        response.getWriter().write(JsonUtils.toJsonString(responseDTO));
    }
}
