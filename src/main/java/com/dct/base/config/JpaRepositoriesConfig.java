package com.dct.base.config;

import com.dct.base.config.properties.JpaConfig;
import com.dct.base.constants.PropertiesConstants;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.List;

/**
 * Specifies the list of packages that Spring will scan and configure for JPA repositories
 * through the value from {@link JpaConfig#getBaseRepositoryPackages()}
 *
 * @author thoaidc
 */
@ConditionalOnProperty(value = PropertiesConstants.JPA_PROPERTIES)
@EnableJpaRepositories(basePackages = "#{@jpaProperties.baseRepositoryPackages.toArray(new String[0])}")
public class JpaRepositoriesConfig {

    private static final Logger log = LoggerFactory.getLogger(JpaRepositoriesConfig.class);
    private final JpaConfig jpaConfig;

    public JpaRepositoriesConfig(@Qualifier("jpaConfig") JpaConfig jpaConfig) {
        this.jpaConfig = jpaConfig;
    }

    /**
     * {@link EntityManagerFactory} is where {@link EntityManager} objects are created <p>
     * Each {@link EntityManager} is a session with the database <p>
     * Used to perform operations like `find`, `persist`, `merge`, `remove`, and queries on entities <p>
     * Each {@link EntityManager} maintains a context for the entity objects and can perform CRUD operations
     */
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("dataSource") DataSource dataSource) {
        List<String> basePackages = jpaConfig.getBaseEntityPackages();
        log.debug("EntityManagerFactory initialized successful. Scan for packages: {}", basePackages);

        return builder.dataSource(dataSource)
                .packages(basePackages.toArray(new String[0])) // Convert List to string array
                .persistenceUnit(jpaConfig.getPersistenceUnitName())
                .build();
    }
}
