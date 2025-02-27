package com.dct.base.security.jwt;

import com.dct.base.config.properties.Security;
import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.SecurityConstants;
import com.dct.base.dto.BaseAuthTokenDTO;
import com.dct.base.exception.BaseAuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String ENTITY_NAME = "JwtTokenProvider";
    private final SecretKey secretKey;
    private final JwtParser jwtParser;
    private final long TOKEN_VALIDITY;
    private final long TOKEN_VALIDITY_FOR_REMEMBER_ME;

    public JwtTokenProvider(@Qualifier("security") Security securityProperties) {
        this.TOKEN_VALIDITY = securityProperties.getTokenValidity();
        this.TOKEN_VALIDITY_FOR_REMEMBER_ME = securityProperties.getTokenValidityForRememberMe();

        String base64SecretKey = securityProperties.getBase64SecretKey();

        if (!StringUtils.hasText(base64SecretKey)) {
            throw new RuntimeException("Secret key not found to sign JWT");
        }

        log.debug("Using a Base64-encoded JWT secret key");
        byte[] keyBytes = Decoders.BASE64.decode(base64SecretKey);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parser().verifyWith(secretKey).build();
        log.debug("Encoded secret key with algorithm: {}", secretKey.getAlgorithm());
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return (String token) -> (Jwt) jwtParser.parse(token);
    }

    public String createToken(BaseAuthTokenDTO authTokenDTO) {
        Authentication authentication = authTokenDTO.getAuthentication();
        Set<String> userAuthorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        long now = (new Date()).getTime();
        Date validity;

        if (authTokenDTO.isRememberMe()) {
            validity = new Date(now + this.TOKEN_VALIDITY_FOR_REMEMBER_ME);
        } else {
            validity = new Date(now + this.TOKEN_VALIDITY);
        }

        return Jwts.builder()
                   .subject(authTokenDTO.getAuthentication().getName())
                   .claim(SecurityConstants.TOKEN_PAYLOAD.USERNAME, authTokenDTO.getUsername())
                   .claim(SecurityConstants.TOKEN_PAYLOAD.USER_ID, authTokenDTO.getUserID())
                   .claim(SecurityConstants.TOKEN_PAYLOAD.DEVICE_ID, authTokenDTO.getDeviceID())
                   .claim(SecurityConstants.TOKEN_PAYLOAD.AUTHORITIES, String.join(",", userAuthorities))
                   .signWith(secretKey)
                   .issuedAt(new Date())
                   .expiration(validity)
                   .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parse(authToken);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SecurityException e) {
            log.error("Invalid JWT token: {} - {}", e.getClass().getName(), e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Token validation error: {} - {}", e.getClass().getName(), e.getMessage());
        }

        throw new BaseAuthenticationException(ENTITY_NAME, ExceptionConstants.TOKEN_INVALID_OR_EXPIRED);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = (Claims) jwtParser.parse(token).getPayload();
        String authorities = (String) claims.get(SecurityConstants.TOKEN_PAYLOAD.AUTHORITIES);

        if (!StringUtils.hasText(authorities)) {
            throw new BaseAuthenticationException(ENTITY_NAME, ExceptionConstants.TOKEN_INVALID_OR_EXPIRED);
        }

        Collection<SimpleGrantedAuthority> userAuthorities = Arrays.stream(authorities.split(","))
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        User principal = new User(claims.getSubject(), "", userAuthorities);
        return new UsernamePasswordAuthenticationToken(principal, token, userAuthorities);
    }
}
