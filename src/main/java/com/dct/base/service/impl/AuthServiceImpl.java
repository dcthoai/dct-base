package com.dct.base.service.impl;

import com.dct.base.common.CredentialGenerator;
import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.constants.ResultConstants;
import com.dct.base.constants.SecurityConstants;
import com.dct.base.dto.BaseAuthTokenDTO;
import com.dct.base.dto.request.AuthRequestDTO;
import com.dct.base.dto.request.RegisterRequestDTO;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.entity.Account;
import com.dct.base.exception.BaseAuthenticationException;
import com.dct.base.exception.BaseBadRequestException;
import com.dct.base.security.model.CustomUserDetails;
import com.dct.base.security.jwt.JwtTokenProvider;
import com.dct.base.security.model.OAuth2TokenResponse;
import com.dct.base.security.model.OAuth2UserInfoResponse;
import com.dct.base.security.service.GoogleOAuth2Service;
import com.dct.base.service.AccountService;
import com.dct.base.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String ENTITY_NAME = "AuthServiceImpl";
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final AccountService accountService;

    public AuthServiceImpl(JwtTokenProvider tokenProvider,
                           AuthenticationManager authenticationManager,
                           GoogleOAuth2Service googleOAuth2Service,
                           AccountService accountService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.googleOAuth2Service = googleOAuth2Service;
        this.accountService = accountService;
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

    @Override
    public BaseResponseDTO authenticateFromGoogle(String code) {
        log.debug("Creating an account for user is authenticate from google");
        OAuth2TokenResponse tokenResponse = googleOAuth2Service.getAccessToken(code);
        OAuth2UserInfoResponse userInfoResponse = googleOAuth2Service.getUserInfo(tokenResponse.getAccessToken());

        String username = CredentialGenerator.generateUsername(8);
        String password = CredentialGenerator.generatePassword(8);
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(username, userInfoResponse.getEmail(), password);
        Account account = accountService.createUserAccount(registerRequestDTO);

        if (Objects.nonNull(account)) {
            Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(SecurityConstants.ROLES.ROLE_USER));
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
            BaseAuthTokenDTO authTokenDTO = new BaseAuthTokenDTO(token, username, account.getId());

            return new BaseResponseDTO(
                HttpStatusConstants.ACCEPTED,
                HttpStatusConstants.STATUS.SUCCESS,
                ResultConstants.LOGIN_SUCCESS,
                tokenProvider.createToken(authTokenDTO)
            );
        }

        throw new BaseAuthenticationException(ENTITY_NAME, ExceptionConstants.REGISTER_FAILED);
    }
}
