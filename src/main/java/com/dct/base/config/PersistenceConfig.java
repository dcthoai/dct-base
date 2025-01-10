package com.dct.base.config;

import com.dct.base.constants.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author thoaidc
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(basePackages = "com.dct.base.repositories")
public class PersistenceConfig {

    private static final Logger log = LoggerFactory.getLogger(PersistenceConfig.class);

    /**
     * Helps JPA automatically handle annotations like @CreatedBy, @LastModifiedBy,... in entities
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

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("dataSource") DataSource dataSource) {
        log.debug("EntityManagerFactory initialized successful");
        return builder.dataSource(dataSource)
                .packages("com.dct.base.entity")
                .persistenceUnit("persistenceUnitName")
                .build();
    }
}
