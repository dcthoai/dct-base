package com.dct.base.core.security.jwt;

import com.dct.base.common.JsonUtils;
import com.dct.base.common.MessageTranslationUtils;
import com.dct.base.constants.BaseHttpStatusConstants;
import com.dct.base.constants.BaseSecurityConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.exception.BaseAuthenticationException;
import com.dct.base.exception.BaseBadRequestException;
import com.dct.base.exception.BaseException;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class BaseJwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(BaseJwtFilter.class);
    private static final String ENTITY_NAME = "BaseJwtFilter";
    private final MessageTranslationUtils messageTranslationUtils;

    protected BaseJwtFilter(MessageTranslationUtils messageTranslationUtils) {
        this.messageTranslationUtils = messageTranslationUtils;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        doFilterCustom(request, response, filterChain);
    }

    protected void doFilterCustom(HttpServletRequest request,
                             HttpServletResponse response,
                             FilterChain filterChain) throws ServletException, IOException {
        if (ifAuthenticationRequired(request)) {
            try {
                String token = resolveToken(request);
                Authentication authentication = validateToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (BaseBadRequestException | BaseAuthenticationException exception) {
                handleException(response, exception);
                return;
            } catch (Exception exception) {
                log.error("BaseJwtFilter unable to process response: {}", exception.getClass().getName(), exception);
                throw exception;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean ifAuthenticationRequired(HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestURI = request.getRequestURI();
        log.info("Filtering {}: {}", request.getMethod(), requestURI);

        return Arrays.stream(publicRequestPatterns()).noneMatch(pattern -> antPathMatcher.match(pattern, requestURI));
    }

    protected String resolveToken(HttpServletRequest request) {
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

    protected void handleException(HttpServletResponse response, BaseException exception) throws IOException {
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

    protected abstract Authentication validateToken(String token);
    protected abstract String[] publicRequestPatterns();
}
