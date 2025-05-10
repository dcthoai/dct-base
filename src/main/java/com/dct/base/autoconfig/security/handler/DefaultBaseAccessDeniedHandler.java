package com.dct.base.autoconfig.security.handler;

import com.dct.base.common.JsonUtils;
import com.dct.base.common.MessageTranslationUtils;
import com.dct.base.constants.BaseExceptionConstants;
import com.dct.base.constants.BaseHttpStatusConstants;
import com.dct.base.core.security.handler.BaseAccessDeniedHandler;
import com.dct.base.dto.response.BaseResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnMissingBean(value = BaseAccessDeniedHandler.class)
public class DefaultBaseAccessDeniedHandler extends BaseAccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultBaseAccessDeniedHandler.class);
    private final MessageTranslationUtils messageTranslationUtils;

    public DefaultBaseAccessDeniedHandler(MessageTranslationUtils messageTranslationUtils) {
        this.messageTranslationUtils = messageTranslationUtils;
        log.debug("`DefaultBaseAccessDeniedHandler` has been automatically configured to handle access denied exceptions");
    }

    @Override
    protected void handleException(HttpServletRequest request,
                                   HttpServletResponse response,
                                   AccessDeniedException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Convert response body to JSON
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(BaseHttpStatusConstants.FORBIDDEN);

        BaseResponseDTO responseDTO = BaseResponseDTO.builder()
                .code(BaseHttpStatusConstants.FORBIDDEN)
                .success(BaseHttpStatusConstants.STATUS.FAILED)
                .message(messageTranslationUtils.getMessageI18n(BaseExceptionConstants.FORBIDDEN))
                .build();

        response.getWriter().write(JsonUtils.toJsonString(responseDTO));
        response.flushBuffer();
    }
}
