package com.dct.base.service.impl;

import com.dct.base.common.CredentialGenerator;
import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.constants.PropertiesConstants;
import com.dct.base.constants.ResultConstants;
import com.dct.base.constants.SecurityConstants;
import com.dct.base.dto.BaseAuthTokenDTO;
import com.dct.base.dto.request.RegisterRequestDTO;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.entity.Account;
import com.dct.base.exception.BaseAuthenticationException;
import com.dct.base.security.jwt.JwtProvider;
import com.dct.base.security.model.OAuth2TokenResponse;
import com.dct.base.security.model.OAuth2UserInfoResponse;
import com.dct.base.security.service.GoogleOAuth2Service;
import com.dct.base.service.AccountService;
import com.dct.base.service.GoogleAuthenticationService;

import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@Service
@ConditionalOnProperty(name = PropertiesConstants.OAUTH2_ACTIVE_STATUS, havingValue = "true")
public class GoogleAuthenticationServiceImpl implements GoogleAuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(GoogleAuthenticationServiceImpl.class);
    private final AccountService accountService;
    private final JwtProvider tokenProvider;
    private final GoogleOAuth2Service googleOAuth2Service;

    public GoogleAuthenticationServiceImpl(AccountService accountService,
                                           JwtProvider tokenProvider,
                                           GoogleOAuth2Service googleOAuth2Service) {
        this.accountService = accountService;
        this.tokenProvider = tokenProvider;
        this.googleOAuth2Service = googleOAuth2Service;
    }

    @Override
    public BaseResponseDTO authorize(String code) {
        log.debug("Get authorization information from Google");

        if (!StringUtils.hasText(code))
            throw new BaseAuthenticationException(ENTITY_NAME, ExceptionConstants.BAD_CREDENTIALS);

        OAuth2TokenResponse tokenResponse = googleOAuth2Service.getAccessToken(code);
        OAuth2UserInfoResponse userInfo = googleOAuth2Service.getUserInfo(tokenResponse.getAccessToken());

        return authorize(userInfo);
    }

    @Override
    public BaseResponseDTO authorize(OAuth2UserInfoResponse userInfo) {
        log.debug("Authorize for user '{}'", userInfo.getEmail());
        Account account = accountService.findUserByEmail(userInfo.getEmail());
        String username, password;

        if (Objects.isNull(account)) {
            username = CredentialGenerator.generateUsername(8);
            password = CredentialGenerator.generatePassword(8);
            log.debug("Authenticate for user '{}' via Google, no account yet", username);
            account = accountService.create(new RegisterRequestDTO(username, userInfo.getEmail(), password));
        } else {
            username = account.getUsername();
        }

        Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(SecurityConstants.ROLES.ROLE_USER));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, username, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
        BaseAuthTokenDTO authTokenDTO = new BaseAuthTokenDTO(token, account);

        log.debug("Authorize successful. Generating token for user '{}'", username);
        String jwtToken = tokenProvider.createToken(authTokenDTO);
        Cookie tokenCookie = new Cookie(SecurityConstants.COOKIES.HTTP_ONLY_COOKIE_ACCESS_TOKEN, jwtToken);

        return new BaseResponseDTO(
            HttpStatusConstants.ACCEPTED,
            HttpStatusConstants.STATUS.SUCCESS,
            ResultConstants.LOGIN_SUCCESS,
            tokenCookie
        );
    }
}
