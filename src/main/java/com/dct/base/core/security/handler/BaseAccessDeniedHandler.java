package com.dct.base.core.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@SuppressWarnings("unused")
public abstract class BaseAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(BaseAccessDeniedHandler.class);

    /**
     * Directly responds to the client when they lack sufficient access rights,
     * without passing the request to further filters <p>
     * In this case, a custom JSON response is sent back <p>
     * You can add additional business logic here, such as sending a redirect or other necessary actions
     *
     * @param request that resulted in an <code>AccessDeniedException</code>
     * @param response so that the user agent can be advised of the failure
     * @param exception that caused the invocation
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {
        log.error("AccessDenied handler is active. {}: {}", exception.getMessage(), request.getRequestURL());
        handleException(request, response, exception);
    }

    protected abstract void handleException(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AccessDeniedException exception) throws IOException;
}
