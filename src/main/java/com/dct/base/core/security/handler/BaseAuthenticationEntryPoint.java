package com.dct.base.core.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@SuppressWarnings("unused")
public abstract class BaseAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(BaseAuthenticationEntryPoint.class);

    /**
     * Directly responds to the client in case of authentication errors without passing the request to further filters<p>
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
        handleException(request, response, authException);
    }

    protected abstract void handleException(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException authException) throws IOException;
}
