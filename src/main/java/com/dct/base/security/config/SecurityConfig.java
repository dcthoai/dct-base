package com.dct.base.security.config;

import com.dct.base.constants.SecurityConstants;
import com.dct.base.security.exception.CustomAccessDeniedHandler;
import com.dct.base.security.exception.CustomAuthenticationEntryPoint;
import com.dct.base.security.service.CustomUserDetailsService;
import com.dct.base.security.jwt.JwtTokenFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final CorsFilter corsFilter;
    private final JwtDecoder jwtDecoder;
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final OAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver;

    public SecurityConfig(CorsFilter corsFilter,
                          JwtDecoder jwtDecoder,
                          JwtTokenFilter jwtTokenFilter,
                          CustomAuthenticationEntryPoint authenticationEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler,
                          OAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver) {
        this.corsFilter = corsFilter;
        this.jwtDecoder = jwtDecoder;
        this.jwtTokenFilter = jwtTokenFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.oAuth2AuthorizationRequestResolver = oAuth2AuthorizationRequestResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("Configuring SecurityFilterChain");
        http.csrf(AbstractHttpConfigurer::disable) // Because of using JWT, CSRF is not required
            .cors(Customizer.withDefaults())
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(corsFilter, JwtTokenFilter.class)
            .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(authenticationEntryPoint))
            .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler))
            .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                .contentSecurityPolicy(policy -> policy.policyDirectives(SecurityConstants.HEADER.SECURITY_POLICY))
                .referrerPolicy(config -> config.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .permissionsPolicy(config -> config.policy(SecurityConstants.HEADER.PERMISSIONS_POLICY))
            )
            .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(registry ->
                registry.requestMatchers(SecurityConstants.REQUEST_MATCHERS.ADMIN).hasRole(SecurityConstants.ROLES.ADMIN)
                .requestMatchers(SecurityConstants.REQUEST_MATCHERS.USER).hasAnyRole(SecurityConstants.ROLES.USER, SecurityConstants.ROLES.ADMIN)
                .requestMatchers(SecurityConstants.REQUEST_MATCHERS.PUBLIC).permitAll()
                // Used with custom CORS filters in CORS (Cross-Origin Resource Sharing) mechanism.
                // The browser will send OPTIONS requests (preflight requests) to check
                // if the server allows access from other sources before send requests such as POST.
                .requestMatchers(HttpMethod.OPTIONS, SecurityConstants.REQUEST_MATCHERS.OPTIONS).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oAuth2Config -> oAuth2Config.authorizationEndpoint(config ->
                config.authorizationRequestResolver(oAuth2AuthorizationRequestResolver)
            ))
            .oauth2ResourceServer(config -> config.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)));

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
