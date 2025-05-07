package com.dct.base.security.config;

import com.dct.base.config.CorsFilterConfig;
import com.dct.base.constants.SecurityConstants;
import com.dct.base.security.handler.CustomAccessDeniedHandler;
import com.dct.base.security.handler.CustomAuthenticationEntryPoint;
import com.dct.base.security.jwt.JwtFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
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
@EnableWebSecurity // Enable the security feature of Spring Security, allowing configuration of security for the application
@EnableMethodSecurity(securedEnabled = true) // Allows the use of method-level security annotations like @Secured or @PreAuthorize
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private final CorsFilter corsFilter;
    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(CorsFilter corsFilter,
                          JwtFilter jwtFilter,
                          CustomAccessDeniedHandler accessDeniedHandler,
                          CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.corsFilter = corsFilter;
        this.jwtFilter = jwtFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    /**
     * This method configures the security filter chain, which includes:
     * <ul>
     *   <li>Disables CSRF (Cross-Site Request Forgery) because uses JWT (which typically does not require CSRF)</li>
     *   <li>Register the {@link CorsFilterConfig#corsFilter()} for use</li>
     *   <li>
     *     Config filters:
     *     <ul>
     *       <li>Places the {@link CorsFilterConfig#corsFilter()} before the {@link JwtFilter}</li>
     *       <li>Places the {@link JwtFilter} before the {@link UsernamePasswordAuthenticationFilter}</li>
     *     </ul>
     *   </li>
     *   <li>
     *     Configures authentication exception handling:
     *     <ul>
     *       <li>Using {@link CustomAuthenticationEntryPoint} for authentication errors</li>
     *       <li>Using {@link CustomAccessDeniedHandler} for access-denied situations</li>
     *     </ul>
     *   </li>
     *   <li>
     *     Configures HTTP security headers to enhance security like:
     *     <ul>
     *       <li>
     *         The `X-Frame-Options` header is used to prevent click jacking attacks <p>
     *         {@link FrameOptionsConfig#sameOrigin} means that the page can only be embedded in an iframe
     *         if the origin of the page matches the origin of the main page, help prevent click jacking attacks
     *       </li>
     *       <li>
     *         The `Content-Security-Policy` (CSP) header helps prevent Cross-Site Scripting (XSS) and data injection attacks
     *         by specifying which content sources are allowed to be loaded by the browser <p>
     *         Policy are listed in {@link SecurityConstants.HEADER#SECURITY_POLICY}
     *       </li>
     *       <li>
     *         The `Referrer-Policy` header controls the information about the referrer (the source of the request)
     *         that is sent when a user clicks a link or loads a resource from a page <p>
     *         {@link ReferrerPolicy#STRICT_ORIGIN_WHEN_CROSS_ORIGIN}
     *         means that the referrer information will only be sent
     *         when transitioning from one HTTPS page to another HTTPS page
     *       </li>
     *       <li>
     *         The `Permissions-Policy` header allows a website to control access to various web APIs and features <p>
     *         The detailed regulations are listed in {@link SecurityConstants.HEADER#PERMISSIONS_POLICY}
     *       </li>
     *     </ul>
     *   </li>
     *   <li>
     *     Sets the session management to {@link SessionCreationPolicy#STATELESS} <p>
     *     Meaning the application does not maintain session state (commonly used in API applications)
     *   </li>
     *   <li>Disables the default form-based login feature of Spring Security</li>
     * </ul>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("Configuring SecurityFilterChain");
        http.csrf(AbstractHttpConfigurer::disable) // Because of using JWT, CSRF is not required
                .cors(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsFilter, JwtFilter.class)
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .headers(header -> header
                        .frameOptions(FrameOptionsConfig::sameOrigin)
                        .contentSecurityPolicy(policy -> policy.policyDirectives(SecurityConstants.HEADER.SECURITY_POLICY))
                        .referrerPolicy(config -> config.policy(ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        .permissionsPolicy(config -> config.policy(SecurityConstants.HEADER.PERMISSIONS_POLICY))
                )
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry
                        // Used with custom CORS filters in CORS (Cross-Origin Resource Sharing) mechanism
                        // The browser will send OPTIONS requests (preflight requests) to check
                        // if the server allows access from other sources before send requests such as POST, GET
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    /**
     * This is a bean that provides an AuthenticationManager from Spring Security, used to authenticate users
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(SecurityConstants.BCRYPT_COST_FACTOR);
    }
}
