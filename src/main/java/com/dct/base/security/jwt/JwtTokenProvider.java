package com.dct.base.security.jwt;

import com.dct.base.constants.AuthConstants;
import com.dct.base.dto.BaseAuthTokenDTO;
import com.dct.base.repositories.AuthorityRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final SecretKey secretKey;
    private final JwtParser jwtParser;
    private final AuthorityRepository authorityRepository;

    @Value("${security.authentication.jwt.token-validity-in-milliseconds}")
    private long TOKEN_EXPIRED_AFTER;

    @Value("${security.authentication.jwt.token-validity-in-milliseconds-for-remember-me}")
    private long TOKEN_FOR_REMEMBER_ME_EXPIRED_AFTER;

    public JwtTokenProvider(Environment env, AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
        String jwtSecretKey = env.getProperty("security.authentication.jwt.base64-secret");

        if (!StringUtils.hasText(jwtSecretKey)) {
            throw new RuntimeException("Secret key not found to sign JWT");
        }

        log.debug("Using a Base64-encoded JWT secret key");
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parser().verifyWith(secretKey).build();
        log.debug("Encoded secret key with algorithm: {}", secretKey.getAlgorithm());
    }

    public String createToken(BaseAuthTokenDTO baseAuthTokenDTO) {
        long now = (new Date()).getTime();
        Date validity;

        if (baseAuthTokenDTO.isRememberMe()) {
            validity = new Date(now + this.TOKEN_FOR_REMEMBER_ME_EXPIRED_AFTER);
        } else {
            validity = new Date(now + this.TOKEN_EXPIRED_AFTER);
        }

        return Jwts.builder()
                   .subject(baseAuthTokenDTO.getAuthentication().getName())
                   .claim(AuthConstants.TOKEN_PAYLOAD.USERNAME, baseAuthTokenDTO.getUsername())
                   .claim(AuthConstants.TOKEN_PAYLOAD.USER_ID, baseAuthTokenDTO.getID())
                   .claim(AuthConstants.TOKEN_PAYLOAD.DEVICE_ID, baseAuthTokenDTO.getDeviceID())
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
            log.error("Invalid JWT token. ", e);
        } catch (IllegalArgumentException e) {
            log.error("Token validation error {}", e.getMessage());
        }

        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = (Claims) jwtParser.parse(token).getPayload();
        int userID = (int) claims.get(AuthConstants.TOKEN_PAYLOAD.USER_ID);
        Set<String> authorities = authorityRepository.findAllByUserID(userID);

        Collection<? extends GrantedAuthority> userAuthorities = authorities.stream()
                .filter(auth -> StringUtils.hasText(auth.trim()))
                .map(SimpleGrantedAuthority::new)
                .toList();

        User principal = new User(claims.getSubject(), "", userAuthorities);
        return new UsernamePasswordAuthenticationToken(principal, token, userAuthorities);
    }
}
