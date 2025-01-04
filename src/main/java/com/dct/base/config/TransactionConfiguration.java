package com.dct.base.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Used to manage transactions with the database. Uses Hibernate/JPA as the ORM (Object-Relational Mapping) tool <p>
 * TransactionManagement: Defines and provides the tools needed to process transactions such as:
 * <ul>
 *     <li>Starting and ending transactions</li>
 *     <li>Managing rollbacks (on errors) and commits (on success)</li>
 * </ul>
 * Customizing transaction parameters: Such as isolation level and propagation behavior <p>
 * Integrating Spring Transaction Management with JPA: Use JpaTransactionManager to connect to JPA
 * @author thoaidc
 */
@Configuration
@EnableTransactionManagement // Enable annotations-based transaction management (@Transactional) in Spring application
public class TransactionConfiguration {

    // This is the main component of JPA, which is automatically initialized by Spring based on the ORM configuration
    // It provides EntityManagers to manipulate the database
    private final EntityManagerFactory entityManagerFactory;

    public TransactionConfiguration(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Has the ability to manage opening, committing and rolling back transactions based on EntityManager <p>
     * Automatically integrates with @Transactional in the application
     * @return {@link PlatformTransactionManager}
     */
    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    /**
     * Provides a programmatic API to manage transactions explicitly (not relying on annotations like @Transactional)
     * @return {@link TransactionTemplate}
     */
    @Bean
    public TransactionTemplate transactionTemplate() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager());
        // Set the transaction isolation level (e.g. ISOLATION_READ_COMMITTED prevents reading uncommitted data)
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        // Set transaction behavior (e.g. PROPAGATION_REQUIRED ensures new transactions will join existing transactions, if any)
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionTemplate;
    }
}
