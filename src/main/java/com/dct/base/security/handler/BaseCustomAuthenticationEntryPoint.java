package com.dct.base.security.handler;

import com.dct.base.common.JsonUtils;
import com.dct.base.common.MessageTranslationUtils;
import com.dct.base.constants.BaseExceptionConstants;
import com.dct.base.constants.BaseHttpStatusConstants;
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

/**
 * Handles unauthenticated requests in a Spring Security application,
 * such as providing valid credentials, handling expired credentials, and managing authentication exceptions
 *
 * @author thoaidc
 */
@Component
public class BaseCustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(BaseCustomAuthenticationEntryPoint.class);
    private final MessageTranslationUtils messageTranslationUtils;

    public BaseCustomAuthenticationEntryPoint(MessageTranslationUtils messageTranslationUtils) {
        this.messageTranslationUtils = messageTranslationUtils;
        log.debug("Configure `BaseCustomAuthenticationEntryPoint` as a default AuthenticationEntryPoint");
    }

    /**
     * Directly responds to the client in case of authentication errors without passing the request to further filters <p>
     * In this case, a custom JSON response is sent back <p>
     * You can add additional business logic here, such as sending a redirect or logging failed login attempts, etc.
     *
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Authentication entry point is active. {}: {}", authException.getMessage(), request.getRequestURL());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Convert response body to JSON
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(BaseHttpStatusConstants.UNAUTHORIZED);

        BaseResponseDTO responseDTO = BaseResponseDTO.builder()
            .code(BaseHttpStatusConstants.UNAUTHORIZED)
            .success(BaseHttpStatusConstants.STATUS.FAILED)
            .message(messageTranslationUtils.getMessageI18n(BaseExceptionConstants.UNAUTHORIZED))
            .build();

        response.getWriter().write(JsonUtils.toJsonString(responseDTO));
        response.flushBuffer();
    }
}
