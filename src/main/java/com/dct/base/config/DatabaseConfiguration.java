package com.dct.base.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);
    private final Environment env;

    public DatabaseConfiguration(Environment env) {
        this.env = env;
    }

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        log.debug("Configuring data source");
        HikariConfig config = new HikariConfig();
        Properties properties = new Properties();

        boolean autoCommit = env.getProperty("spring.datasource.hikari.auto-commit", Boolean.class, false);
        int maxPoolSize = env.getProperty("spring.datasource.hikari.maximumPoolSize", Integer.class, 15);
        int minIdle = env.getProperty("spring.datasource.hikari.minimumIdle", Integer.class, 5);
        long idleTimeout = env.getProperty("spring.datasource.hikari.idleTimeout", Long.class, 180000L);
        long maxLifetime = env.getProperty("spring.datasource.hikari.maxLifetime", Long.class, 1800000L);
        long cntTimeout = env.getProperty("spring.datasource.hikari.connectionTimeout", Long.class, 200000L);
        String poolName = env.getProperty("spring.datasource.hikari.poolName", "HikariPool");
        String cachePrepStmts = env.getProperty("spring.datasource.hikari.data-source-properties.cachePrepStmts", "true");
        String cacheSize = env.getProperty("spring.datasource.hikari.data-source-properties.prepStmtCacheSize", "250");
        String cacheLimit = env.getProperty("spring.datasource.hikari.data-source-properties.prepStmtCacheSqlLimit", "2048");
        String useServerPrepStmts = env.getProperty("spring.datasource.hikari.data-source-properties.useServerPrepStmts", "true");

        properties.setProperty("cachePrepStmts", cachePrepStmts);
        properties.setProperty("prepStmtCacheSize", cacheSize);
        properties.setProperty("prepStmtCacheSqlLimit", cacheLimit);
        properties.setProperty("useServerPrepStmts", useServerPrepStmts);
        properties.setProperty("passwordCharacterEncoding", "UTF-8");
        properties.setProperty("serverTimezone", "UTC");

        config.setJdbcUrl(env.getProperty("spring.datasource.url"));
        config.setUsername(env.getProperty("spring.datasource.username"));
        config.setPassword(env.getProperty("spring.datasource.password"));

        config.setAutoCommit(autoCommit);
        config.setAllowPoolSuspension(false);
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setConnectionTimeout(cntTimeout);
        config.setPoolName(poolName);
        config.setDataSourceProperties(properties);

        return new HikariDataSource(config);
    }

    @Primary
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
