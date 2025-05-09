package com.dct.base.security.jwt;

import com.dct.base.common.JsonUtils;
import com.dct.base.common.MessageTranslationUtils;
import com.dct.base.constants.BaseHttpStatusConstants;
import com.dct.base.constants.BaseSecurityConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.exception.BaseAuthenticationException;
import com.dct.base.exception.BaseBadRequestException;
import com.dct.base.exception.BaseException;
import com.dct.base.security.config.SecurityAuthorizeHttpRequest;

import jakarta.annotation.Nonnull;
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
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private static final String ENTITY_NAME = "JwtTokenFilter";
    private final JwtProvider jwtProvider;
    private final MessageTranslationUtils messageTranslationUtils;
    private final String[] securityPublicApiPatterns;

    public JwtFilter(JwtProvider jwtProvider,
                     MessageTranslationUtils messageTranslationUtils,
                     SecurityAuthorizeHttpRequest securityAuthorizeHttpRequest) {
        this.jwtProvider = jwtProvider;
        this.messageTranslationUtils = messageTranslationUtils;
        this.securityPublicApiPatterns = securityAuthorizeHttpRequest.getPublicPatterns();
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        if (ifAuthenticationRequired(request)) {
            try {
                String token = resolveToken(request);
                Authentication authentication = this.jwtProvider.validateToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (BaseBadRequestException | BaseAuthenticationException exception) {
                handleException(response, exception);
                return;
            } catch (Exception exception) {
                log.error("JwtFilter unable to process response for: {}", exception.getClass().getName(), exception);
                throw exception;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean ifAuthenticationRequired(HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestURI = request.getRequestURI();
        log.info("Filtering {}: {}", request.getMethod(), requestURI);
        return Arrays.stream(securityPublicApiPatterns).noneMatch(pattern -> antPathMatcher.match(pattern, requestURI));
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String bearerToken = null;

        if (Objects.nonNull(cookies)) {
            bearerToken = Arrays.stream(cookies)
                    .filter(cookie -> BaseSecurityConstants.COOKIES.HTTP_ONLY_COOKIE_ACCESS_TOKEN.equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        if (!StringUtils.hasText(bearerToken))
            bearerToken = request.getHeader(BaseSecurityConstants.HEADER.AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(bearerToken))
            bearerToken = request.getHeader(BaseSecurityConstants.HEADER.AUTHORIZATION_GATEWAY_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BaseSecurityConstants.HEADER.TOKEN_TYPE))
            return bearerToken.substring(7);

        return bearerToken;
    }

    private void handleException(HttpServletResponse response, BaseException exception) throws IOException {
        log.error("[{}] - Handling exception {}", ENTITY_NAME, exception.getClass().getName(), exception);
        response.setStatus(BaseHttpStatusConstants.UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        BaseResponseDTO responseDTO = BaseResponseDTO.builder()
            .code(BaseHttpStatusConstants.UNAUTHORIZED)
            .success(BaseHttpStatusConstants.STATUS.FAILED)
            .message(messageTranslationUtils.getMessageI18n(exception.getErrorKey(), exception.getArgs()))
            .build();

        response.getWriter().write(JsonUtils.toJsonString(responseDTO));
        response.flushBuffer();
    }
}
