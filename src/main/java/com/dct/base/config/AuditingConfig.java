package com.dct.base.config;

import com.dct.base.constants.BaseConstants;
import com.dct.base.constants.PropertiesConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

/**
 * JPA (Java Persistence API) configurations <p>
 * {@link EnableJpaAuditing} help JPA fills in information about the creator, the modifier in the entities <p>
 * {@link EnableJpaRepositories} allows Spring to auto find and create repositories for entities in the basePackages <p>
 * Helping to reduce the need to write code for basic CRUD operations
 *
 * @author thoaidc
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@ConditionalOnProperty(value = PropertiesConstants.JPA_PROPERTIES)
public class AuditingConfig {

    private static final Logger log = LoggerFactory.getLogger(AuditingConfig.class);

    /**
     * Helps JPA automatically handle annotations like @{@link CreatedBy}, @{@link LastModifiedBy},... in entities
     * @return {@link AuditorAware}
     */
    @Bean(name = "auditorProvider")
    public AuditorAware<String> auditorProvider() {
        log.debug("AuditorProvider initialized successful");

        return () -> {
            // Get the current username from the SecurityContext, using a default value if no user is authenticated
            String credential = SecurityContextHolder.getContext().getAuthentication().getName();
            String createdBy = Objects.equals(BaseConstants.ANONYMOUS_USER, credential) ? null : credential;
            return Optional.of(Optional.ofNullable(createdBy).orElse(BaseConstants.DEFAULT_CREATOR));
        };
    }
}
