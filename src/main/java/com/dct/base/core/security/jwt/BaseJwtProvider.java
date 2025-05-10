package com.dct.base.core.security.jwt;

import com.dct.base.config.properties.BaseSecurityProperties;
import com.dct.base.constants.BaseExceptionConstants;
import com.dct.base.constants.BaseSecurityConstants;
import com.dct.base.dto.auth.BaseAuthTokenDTO;
import com.dct.base.exception.BaseAuthenticationException;
import com.dct.base.exception.BaseBadRequestException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class BaseJwtProvider {

    private static final Logger log = LoggerFactory.getLogger(BaseJwtProvider.class);
    private static final String ENTITY_NAME = "BaseJwtProvider";
    private final SecretKey secretKey;
    private final JwtParser jwtParser;
    private final long TOKEN_VALIDITY;
    private final long TOKEN_VALIDITY_FOR_REMEMBER_ME;

    public BaseJwtProvider(BaseSecurityProperties security) {
        this.TOKEN_VALIDITY = security.getTokenValidity();
        this.TOKEN_VALIDITY_FOR_REMEMBER_ME = security.getTokenValidityForRememberMe();
        String base64SecretKey = security.getBase64SecretKey();

        if (!StringUtils.hasText(base64SecretKey)) {
            throw new RuntimeException("Could not found secret key to sign JWT");
        }

        log.debug("Using a Base64-encoded JWT secret key");
        byte[] keyBytes = Base64.getUrlDecoder().decode(base64SecretKey);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parser().verifyWith(secretKey).build();
        log.debug("Sign JWT with algorithm: {}", secretKey.getAlgorithm());
    }

    public String createToken(BaseAuthTokenDTO authTokenDTO) {
        Authentication authentication = authTokenDTO.getAuthentication();
        Set<String> userAuthorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        long validityInMilliseconds = Instant.now().toEpochMilli();

        if (authTokenDTO.isRememberMe())
            validityInMilliseconds += this.TOKEN_VALIDITY_FOR_REMEMBER_ME;
        else
            validityInMilliseconds += this.TOKEN_VALIDITY;

        return Jwts.builder()
                .subject(authTokenDTO.getAuthentication().getName())
                .claim(BaseSecurityConstants.TOKEN_PAYLOAD.USER_ID, authTokenDTO.getUserId())
                .claim(BaseSecurityConstants.TOKEN_PAYLOAD.USERNAME, authTokenDTO.getUsername())
                .claim(BaseSecurityConstants.TOKEN_PAYLOAD.AUTHORITIES, String.join(",", userAuthorities))
                .signWith(secretKey)
                .issuedAt(new Date())
                .expiration(new Date(validityInMilliseconds))
                .compact();
    }

    public Authentication validateToken(String authToken) {
        if (!StringUtils.hasText(authToken))
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.BAD_CREDENTIALS);

        try {
            return getAuthentication(authToken);
        } catch (MalformedJwtException e) {
            log.error("[{}] - Invalid JWT: {}", ENTITY_NAME, e.getMessage());
        } catch (SignatureException e) {
            log.error("[{}] - Invalid JWT signature: {}", ENTITY_NAME, e.getMessage());
        } catch (SecurityException e) {
            log.error("[{}] - Unable to decode JWT: {}", ENTITY_NAME, e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("[{}] - Expired JWT: {}", ENTITY_NAME, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("[{}] - Invalid JWT string (null, empty,...): {}", ENTITY_NAME, e.getMessage());
        }

        throw new BaseAuthenticationException(ENTITY_NAME, BaseExceptionConstants.TOKEN_INVALID_OR_EXPIRED);
    }

    protected Authentication getAuthentication(String token) {
        Claims claims = (Claims) jwtParser.parse(token).getPayload();
        String authorities = (String) claims.get(BaseSecurityConstants.TOKEN_PAYLOAD.AUTHORITIES);

        if (!StringUtils.hasText(authorities)) {
            throw new BaseAuthenticationException(ENTITY_NAME, BaseExceptionConstants.FORBIDDEN);
        }

        Collection<SimpleGrantedAuthority> userAuthorities = Arrays.stream(authorities.split(","))
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        User principal = new User(claims.getSubject(), "none-password", userAuthorities);
        return new UsernamePasswordAuthenticationToken(principal, token, userAuthorities);
    }
}
