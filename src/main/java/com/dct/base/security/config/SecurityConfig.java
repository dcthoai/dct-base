package com.dct.base.security.config;

import com.dct.base.constants.SecurityConstants;
import com.dct.base.security.exception.CustomAccessDeniedHandler;
import com.dct.base.security.exception.CustomAuthenticationEntryPoint;
import com.dct.base.security.service.CustomUserDetailsService;
import com.dct.base.security.jwt.JwtTokenFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;

import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final CorsFilter corsFilter;
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final OAuth2ClientConfig oAuth2ClientConfig;

    public SecurityConfig(CorsFilter corsFilter,
                          JwtTokenFilter jwtTokenFilter,
                          CustomAccessDeniedHandler accessDeniedHandler,
                          CustomAuthenticationEntryPoint authenticationEntryPoint,
                          @Autowired(required = false) OAuth2ClientConfig oAuth2ClientConfig) {
        this.corsFilter = corsFilter;
        this.jwtTokenFilter = jwtTokenFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.oAuth2ClientConfig = oAuth2ClientConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("Configuring SecurityFilterChain");
        http.csrf(AbstractHttpConfigurer::disable) // Because of using JWT, CSRF is not required
            .cors(Customizer.withDefaults())
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(corsFilter, JwtTokenFilter.class)
            .exceptionHandling(handler -> handler
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )
            .headers(header -> header
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                .contentSecurityPolicy(policy -> policy.policyDirectives(SecurityConstants.HEADER.SECURITY_POLICY))
                .referrerPolicy(config -> config.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .permissionsPolicy(config -> config.policy(SecurityConstants.HEADER.PERMISSIONS_POLICY))
            )
            .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(registry -> registry
                .requestMatchers(SecurityConstants.REQUEST_MATCHERS.ADMIN).hasRole(SecurityConstants.ROLES.ADMIN)
                .requestMatchers(SecurityConstants.REQUEST_MATCHERS.USER).hasAnyRole(
                    SecurityConstants.ROLES.USER,
                    SecurityConstants.ROLES.ADMIN
                )
                .requestMatchers(SecurityConstants.REQUEST_MATCHERS.PUBLIC).permitAll()
                // Used with custom CORS filters in CORS (Cross-Origin Resource Sharing) mechanism.
                // The browser will send OPTIONS requests (preflight requests) to check
                // if the server allows access from other sources before send requests such as POST.
                .requestMatchers(HttpMethod.OPTIONS, SecurityConstants.REQUEST_MATCHERS.OPTIONS).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(AbstractHttpConfigurer::disable);

        if (Objects.nonNull(oAuth2ClientConfig))
            oAuth2ClientConfig.configure(http);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(SecurityConstants.BCRYPT_COST_FACTOR);
    }
}
