package com.dct.base.security.jwt;

import com.dct.base.constants.SecurityConstants;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is found
 * @author thoaidc
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request,
                                    @Nullable HttpServletResponse response,
                                    @Nullable FilterChain filterChain) throws ServletException, IOException {
        if (Objects.nonNull(request)) {
            String jwt = resolveToken(request);

            if (StringUtils.hasText(jwt) && this.jwtTokenProvider.validateToken(jwt)) {
                Authentication authentication = this.jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        if (Objects.nonNull(filterChain))
            filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.HEADER.AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(bearerToken)) {
            bearerToken = request.getHeader(SecurityConstants.HEADER.AUTHORIZATION_GATEWAY_HEADER);
        }

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstants.HEADER.TOKEN_TYPE)) {
            return bearerToken.substring(7);
        }

        return null;
    }
}