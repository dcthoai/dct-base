package com.dct.base.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

/**
 * A class interceptor in Spring is used to handle HTTP requests before they are passed to the controller <p>
 * Implements {@link HandlerInterceptor} helps perform actions before, after, or during the handling of HTTP requests<p>
 * Allows for additional actions to be performed on the request and response <p>
 * For example: logging or monitoring system performance
 *
 * @author thoaidc
 */
@Component
public class BaseHandlerInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(BaseHandlerInterceptor.class);
    private static final String ENTITY_NAME = "BaseHandlerInterceptor";

    /**
     * It is called before the HTTP request is forwarded to the controller <p>
     * (i.e., before @{@link RequestMapping} or similar annotations are called)
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return true if allowing the request to continue and be forwarded to the controller for processing
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        return true;
    }

    /**
     * It is called after the controller has processed the request and before the response is returned to the client
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler (or {@link HandlerMethod}) that started asynchronous execution,
     *                for type and/or instance examination
     * @param mav the {@link ModelAndView} that the handler returned (can also be {@code null})
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) {

    }
}
