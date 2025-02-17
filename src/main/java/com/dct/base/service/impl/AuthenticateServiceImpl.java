package com.dct.base.service.impl;

import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.constants.ResultConstants;
import com.dct.base.constants.SecurityConstants;
import com.dct.base.dto.BaseAuthTokenDTO;
import com.dct.base.dto.request.AuthRequestDTO;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.exception.BaseAuthenticationException;
import com.dct.base.exception.BaseBadRequestException;
import com.dct.base.security.model.CustomUserDetails;
import com.dct.base.security.jwt.JwtTokenProvider;
import com.dct.base.service.AuthenticationService;

import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticateServiceImpl.class);
    private static final String ENTITY_NAME = "AuthServiceImpl";
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticateServiceImpl(JwtTokenProvider tokenProvider,
                           AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public BaseResponseDTO authenticate(AuthRequestDTO authRequestDTO) {
        log.debug("Authenticating user: {}", authRequestDTO.getUsername());
        String username = authRequestDTO.getUsername().trim();
        String rawPassword = authRequestDTO.getPassword().trim();
        Authentication authentication = authenticate(username, rawPassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        BaseAuthTokenDTO authTokenDTO = new BaseAuthTokenDTO(
            authentication,
            userDetails.getAccount(),
            authRequestDTO.getDeviceID(),
            authRequestDTO.getRememberMe()
        );

        String jwtToken = tokenProvider.createToken(authTokenDTO);
        Cookie tokenCookie = new Cookie(SecurityConstants.COOKIES.HTTP_ONLY_COOKIE_ACCESS_TOKEN, jwtToken);

        return new BaseResponseDTO(
            HttpStatusConstants.ACCEPTED,
            HttpStatusConstants.STATUS.SUCCESS,
            ResultConstants.LOGIN_SUCCESS,
            tokenCookie
        );
    }

    private Authentication authenticate(String username, String rawPassword) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, rawPassword);

        try {
            return authenticationManager.authenticate(token);
        } catch (UsernameNotFoundException e) {
            log.error("[{}] Account '{}' does not exists", e.getClass().getSimpleName(), username);
            throw new BaseBadRequestException(ENTITY_NAME, ExceptionConstants.ACCOUNT_NOT_FOUND);
        } catch (BadCredentialsException e) {
            log.error("[{}] - {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BaseBadRequestException(ENTITY_NAME, ExceptionConstants.BAD_CREDENTIALS);
        } catch (CredentialsExpiredException e) {
            log.error("[{}] - {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BaseBadRequestException(ENTITY_NAME, ExceptionConstants.ACCOUNT_EXPIRED);
        } catch (AuthenticationException e) {
            log.error("[{}] Authentication failed: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BaseAuthenticationException(ENTITY_NAME, ExceptionConstants.UNAUTHORIZED);
        }
    }
}
