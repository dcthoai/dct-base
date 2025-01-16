package com.dct.base.service.impl;

import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.constants.ResultConstants;
import com.dct.base.dto.BaseAuthTokenDTO;
import com.dct.base.dto.request.AuthRequestDTO;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.entity.Account;
import com.dct.base.exception.BaseAuthenticationException;
import com.dct.base.security.CustomUserDetails;
import com.dct.base.security.jwt.JwtTokenProvider;
import com.dct.base.service.AuthService;

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
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String ENTITY_NAME = "AuthServiceImpl";
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(JwtTokenProvider tokenProvider,
                           AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public BaseResponseDTO authenticate(AuthRequestDTO authRequestDTO) {
        log.debug("Authenticating user: {}", authRequestDTO.getUsername());

        String username = authRequestDTO.getUsername().trim();
        String rawPassword = authRequestDTO.getPassword().trim();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, rawPassword);
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            log.error("[{}.{}] Bad credentials: {}", ENTITY_NAME, e.getMessage(), e.getClass().getName());
            throw new BaseAuthenticationException(ENTITY_NAME, ExceptionConstants.BAD_CREDENTIALS);
        } catch (UsernameNotFoundException e) {
            log.error("[{}.{}] Username not found: {}", ENTITY_NAME, e.getMessage(), e.getClass().getName());
            throw new BaseAuthenticationException(ENTITY_NAME, ExceptionConstants.ACCOUNT_NOT_FOUND);
        } catch (CredentialsExpiredException e) {
            log.error("[{}.{}] Credentials expired: {}", ENTITY_NAME, e.getMessage(), e.getClass().getName());
            throw new BaseAuthenticationException(ENTITY_NAME, ExceptionConstants.ACCOUNT_EXPIRED);
        } catch (AuthenticationException e) {
            log.error("[{}.{}] Authentication failed: {}", ENTITY_NAME, e.getMessage(), e.getClass().getName());
            throw new BaseAuthenticationException(ENTITY_NAME, ExceptionConstants.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = userDetails.getAccount();
        BaseAuthTokenDTO authTokenDTO = new BaseAuthTokenDTO(authentication, authRequestDTO.getUsername());

        authTokenDTO.setUserID(account.getId());
        authTokenDTO.setDeviceID(authRequestDTO.getDeviceID());
        authTokenDTO.setRememberMe(authRequestDTO.getRememberMe());

        return new BaseResponseDTO(
            HttpStatusConstants.ACCEPTED,
            HttpStatusConstants.STATUS.SUCCESS,
            ResultConstants.LOGIN_SUCCESS,
            tokenProvider.createToken(authTokenDTO)
        );
    }
}
