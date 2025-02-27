package com.dct.base.security.jwt;

import com.dct.base.common.BaseCommon;
import com.dct.base.common.JsonUtils;
import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.constants.SecurityConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.exception.BaseAuthenticationException;
import com.dct.base.constants.SecurityConstants.REQUEST_MATCHERS;

import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is found
 * @author thoaidc
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);
    private static final String ENTITY_NAME = "JwtTokenFilter";
    private final JwtTokenProvider jwtTokenProvider;
    private final BaseCommon baseCommon;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, BaseCommon baseCommon) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.baseCommon = baseCommon;
    }

    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request,
                                    @Nullable HttpServletResponse response,
                                    @Nullable FilterChain filterChain) throws ServletException, IOException {
        if (Objects.isNull(request)) {
            resolveResponse(response, HttpStatusConstants.INTERNAL_SERVER_ERROR, "");
            return;
        }

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestURI = request.getRequestURI();
        log.debug("{}: {}", request.getMethod(), requestURI);

        // If request is required authenticate
        if (Arrays.stream(REQUEST_MATCHERS.PUBLIC).noneMatch(pattern -> antPathMatcher.match(pattern, requestURI))) {
            String jwt = resolveToken(request);

            try {
                if (StringUtils.hasText(jwt) && this.jwtTokenProvider.validateToken(jwt)) {
                    Authentication authentication = this.jwtTokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (BaseAuthenticationException e) {
                log.error("Handling response for {} in: {}", e.getClass().getName(), ENTITY_NAME);
                resolveResponse(
                    response,
                    HttpStatusConstants.UNAUTHORIZED,
                    baseCommon.getMessageI18n(ExceptionConstants.UNAUTHORIZED, e.getArgs())
                );
                return;
            } catch (Exception e) {
                log.error("JwtFilter unable to process response for BaseAuthenticationException", e);
                throw e;
            }
        }

        if (Objects.nonNull(filterChain))
            filterChain.doFilter(request, response);
        else
            resolveResponse(
                response,
                HttpStatusConstants.INTERNAL_SERVER_ERROR,
                baseCommon.getMessageI18n(ExceptionConstants.FILTER_CHAIN_NOT_FOUND)
            );
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String bearerToken = null;

        if (Objects.nonNull(cookies)) {
            bearerToken = Arrays.stream(cookies)
                    .filter(cookie -> SecurityConstants.COOKIES.HTTP_ONLY_COOKIE_ACCESS_TOKEN.equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            if (StringUtils.hasText(bearerToken))
                return bearerToken;
        }

        if (!StringUtils.hasText(bearerToken))
            bearerToken = request.getHeader(SecurityConstants.HEADER.AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(bearerToken))
            bearerToken = request.getHeader(SecurityConstants.HEADER.AUTHORIZATION_GATEWAY_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstants.HEADER.TOKEN_TYPE))
            return bearerToken.substring(7);

        return null;
    }

    private void resolveResponse(HttpServletResponse response, int code, String message) throws IOException {
        if (Objects.isNull(response))
            return;

        response.setStatus(code);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        BaseResponseDTO responseDTO = new BaseResponseDTO(code, HttpStatusConstants.STATUS.FAILED, message);
        response.getWriter().write(JsonUtils.toJsonString(responseDTO));
        response.flushBuffer();
    }
}