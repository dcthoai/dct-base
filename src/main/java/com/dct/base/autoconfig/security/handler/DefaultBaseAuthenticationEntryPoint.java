package com.dct.base.autoconfig.security.handler;

import com.dct.base.common.JsonUtils;
import com.dct.base.common.MessageTranslationUtils;
import com.dct.base.constants.BaseExceptionConstants;
import com.dct.base.constants.BaseHttpStatusConstants;
import com.dct.base.core.security.handler.BaseAuthenticationEntryPoint;
import com.dct.base.dto.response.BaseResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnMissingBean(value = BaseAuthenticationEntryPoint.class)
public class DefaultBaseAuthenticationEntryPoint extends BaseAuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(DefaultBaseAuthenticationEntryPoint.class);
    private final MessageTranslationUtils messageTranslationUtils;

    public DefaultBaseAuthenticationEntryPoint(MessageTranslationUtils messageTranslationUtils) {
        this.messageTranslationUtils = messageTranslationUtils;
        log.debug("`DefaultBaseAuthenticationEntryPoint` has been automatically configured to handle unauthorized exceptions");
    }

    @Override
    protected void handleException(HttpServletRequest request,
                                   HttpServletResponse response,
                                   AuthenticationException authException) throws IOException {
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
