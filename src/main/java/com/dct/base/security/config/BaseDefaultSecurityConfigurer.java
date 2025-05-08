package com.dct.base.security.config;

import com.dct.base.constants.SecurityConstants;
import com.dct.base.security.handler.CustomAccessDeniedHandler;
import com.dct.base.security.handler.CustomAuthenticationEntryPoint;
import com.dct.base.security.jwt.JwtFilter;
import com.dct.base.exception.handler.CustomExceptionHandler;
import com.dct.base.security.utils.SecurityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * This class configures security for a Spring application, include:
 * <ul>
 *   <li>CORS filters</li>
 *   <li>Error handling</li>
 *   <li>Securing HTTP requests</li>
 *   <li>Session management</li>
 *   <li>Detailed access configuration</li>
 * </ul>
 *
 * It also provides methods to configure for user authentication such as:
 * <ul>
 *   <li>{@link AuthenticationManager}</li>
 *   <li>{@link PasswordEncoder}</li>
 * </ul>
 *
 * @author thoaidc
 */
@Configuration
@EnableWebSecurity // Enable the security feature of Spring Security
public class BaseDefaultSecurityConfigurer {

    private static final Logger log = LoggerFactory.getLogger(BaseDefaultSecurityConfigurer.class);
    private final CorsFilter corsFilter;
    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final String[] DEFAULT_PUBLIC_API_PATTERNS = SecurityConstants.REQUEST_MATCHERS.DEFAULT_PUBLIC_API_PATTERNS;

    public BaseDefaultSecurityConfigurer(CorsFilter corsFilter,
                                 JwtFilter jwtFilter,
                                 CustomAccessDeniedHandler accessDeniedHandler,
                                 CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.corsFilter = corsFilter;
        this.jwtFilter = jwtFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        log.debug("Configuring SecurityFilterChain");
    }

    protected void configureCsrf(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable); // Because of using JWT, CSRF is not required
    }

    protected void configureCors(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
    }

    protected void configureFilters(HttpSecurity http) {
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(corsFilter, JwtFilter.class);
    }

    protected void configureExceptionHandler(HttpSecurity http) throws Exception {
        http.exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint)
                                                 .accessDeniedHandler(accessDeniedHandler));
    }

    protected void configureHeaders(HttpSecurity http) throws Exception {
        http.headers(header -> header
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            .contentSecurityPolicy(policy -> policy.policyDirectives(SecurityConstants.HEADER.SECURITY_POLICY))
            .referrerPolicy(config -> config.policy(ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            .permissionsPolicy(config -> config.policy(SecurityConstants.HEADER.PERMISSIONS_POLICY))
        );
    }

    protected void configureSessionManagement(HttpSecurity http) throws Exception {
        http.sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    protected void configureAuthorization(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.authorizeHttpRequests(registry -> registry
            .requestMatchers(SecurityUtils.convertToMvcMatchers(mvc, DEFAULT_PUBLIC_API_PATTERNS))
            .permitAll()
            .requestMatchers(HttpMethod.OPTIONS).permitAll()
            .anyRequest().authenticated()
        );
    }

    protected void configureFormLogin(HttpSecurity http) throws Exception {
        http.formLogin(AbstractHttpConfigurer::disable);
    }

    @SuppressWarnings("unused")
    protected void additionalConfigurationOptions(HttpSecurity http) {}

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        configureCsrf(http);
        configureCors(http);
        configureFilters(http);
        configureExceptionHandler(http);
        configureHeaders(http);
        configureSessionManagement(http);
        configureAuthorization(http, mvc);
        configureFormLogin(http);
        additionalConfigurationOptions(http);
        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    /**
     * Configure a custom AuthenticationProvider to replace the default provider in Spring Security <p>
     * Method `setHideUserNotFoundExceptions` allows {@link UsernameNotFoundException} to be thrown
     * when an account is not found instead of convert to {@link BadCredentialsException} by default <p>
     * After that, the {@link UsernameNotFoundException} will be handle by {@link CustomExceptionHandler}
     */
    @Bean
    @ConditionalOnBean(UserDetailsService.class)
    public DaoAuthenticationProvider defaultAuthenticationProvider(UserDetailsService userDetailsService,
                                                                   PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    /**
     * This is a bean that provides an AuthenticationManager from Spring Security, used to authenticate users
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager defaultAuthenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder defaultPasswordEncoder() {
        return new BCryptPasswordEncoder(SecurityConstants.BCRYPT_COST_FACTOR);
    }
}
