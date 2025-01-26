package com.dct.base.security.handler;

import com.dct.base.common.BaseCommon;
import com.dct.base.common.JsonUtils;
import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.constants.PropertiesConstants;
import com.dct.base.dto.response.BaseResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Handle failures when authenticating through OAuth2 <p>
 * For example, due to an authorization code error or other issues from the OAuth2 provider <p>
 * {@link PropertiesConstants#OAUTH2_ACTIVE_STATUS} helps turn on/off the OAuth2 feature based on the configuration <p>
 * {@link ConditionalOnProperty} mark this class to be initialized only if the property OAUTH2_ACTIVE_STATUS is "true"
 *
 * @author thoaidc
 */
@Component
@ConditionalOnProperty(name = PropertiesConstants.OAUTH2_ACTIVE_STATUS, havingValue = "true")
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationFailureHandler.class);
    private final BaseCommon baseCommon;

    public OAuth2AuthenticationFailureHandler(BaseCommon baseCommon) {
        this.baseCommon = baseCommon;
        log.debug("Configured 'OAuth2AuthenticationFailureHandler' for use");
    }

    /**
     * Called when an error occurs during the OAuth2 authentication process <p>
     * Respond directly to the client through the response object <p>
     * In this case, send a custom JSON response <p>
     * You can add other business logic here, such as sending a redirect
     *
     * @param request the request during which the authentication attempt occurred
     * @param response the response
     * @param exception the exception which was thrown to reject the authentication request
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.error("OAuth2 authentication entrypoint is active. {}: {}", exception.getMessage(), request.getRequestURL());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Convert response body to JSON
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatusConstants.UNAUTHORIZED);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.UNAUTHORIZED,
            HttpStatusConstants.STATUS.FAILED,
            baseCommon.getMessageI18n(ExceptionConstants.OAUTH2_AUTHORIZATION_CODE_EXCEPTION)
        );

        response.getWriter().write(JsonUtils.toJsonString(responseDTO));
        response.flushBuffer();
    }
}
