package com.dct.base.config;

import com.dct.base.constants.BaseConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.util.Optional;

/**
 *
 * @author thoaidc
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(basePackages = "com.dct.base.repositories")
public class PersistenceConfig {

    /**
     * Helps JPA automatically handle annotations like @CreatedBy, @LastModifiedBy,... in entities
     * @return {@link AuditorAware}
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // Get the current username from the SecurityContext, using a default value if no user is authenticated
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            return Optional.of(Optional.ofNullable(creator).orElse(BaseConstants.DEFAULT_CREATOR));
        };
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("dataSource") DataSource dataSource) {
        return builder.dataSource(dataSource)
                .packages("com.dct.base.entity")
                .persistenceUnit("persistenceUnitName")
                .build();
    }
}
