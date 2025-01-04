package com.dct.base.security;

import com.dct.base.constants.AuthConstants;
import com.dct.base.security.jwt.JwtTokenConfigurer;
import com.dct.base.security.jwt.JwtTokenProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(CorsFilter corsFilter,
                          JwtTokenProvider jwtTokenProvider,
                          CustomAuthenticationEntryPoint authenticationEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler) {
        this.corsFilter = corsFilter;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Because of using JWT, CSRF is not required
            .cors(Customizer.withDefaults())
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint))
            .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler))
            .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                .contentSecurityPolicy(policy -> policy.policyDirectives(AuthConstants.HEADER_SECURITY_POLICY))
                .referrerPolicy(config -> config.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .permissionsPolicy(config -> config.policy(AuthConstants.HEADER_PERMISSIONS_POLICY))
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authRequestsRegistry ->
                authRequestsRegistry.requestMatchers("/api/admin/**", "/admin**").hasRole(AuthConstants.ROLE_ADMIN)
                                    .requestMatchers("/api/users/**", "/users**").hasRole(AuthConstants.ROLE_USER)
                                    // Used with custom CORS filters in CORS (Cross-Origin Resource Sharing) mechanism.
                                    // The browser will send OPTIONS requests (preflight requests) to check
                                    // if the server allows access from other sources before send requests such as POST.
                                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                    .requestMatchers("/app/**/*.{js,html}").permitAll()
                                    .requestMatchers("/resources/**").permitAll()
                                    .requestMatchers("/i18n/**").permitAll()
                                    .requestMatchers("/test/**").permitAll()
                                    .requestMatchers("/api/authenticate").permitAll()
                                    .requestMatchers("/api/auth/**").permitAll()
                                    .requestMatchers("/api/p/**").permitAll()
                                    .requestMatchers("/register").permitAll()
                                    .requestMatchers("/login").permitAll()
                                    .requestMatchers("/p/**").permitAll()
                                    .anyRequest().authenticated()
            )
            .apply(new JwtTokenConfigurer(jwtTokenProvider));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
