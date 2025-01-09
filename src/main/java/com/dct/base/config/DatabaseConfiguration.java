package com.dct.base.config;

import com.dct.base.config.properties.Datasource;
import com.dct.base.config.properties.DatasourceProperties;
import com.dct.base.config.properties.Hikari;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);
    private final Datasource datasource;
    private final Hikari hikari;
    private final DatasourceProperties datasourceProperties;

    public DatabaseConfiguration(@Qualifier("datasource") Datasource datasource,
                                 @Qualifier("hikari") Hikari hikari,
                                 @Qualifier("datasourceProperties") DatasourceProperties datasourceProperties) {
        this.datasource = datasource;
        this.hikari = hikari;
        this.datasourceProperties = datasourceProperties;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        log.debug("Configuring data source");
        HikariConfig config = new HikariConfig();
        Properties props = new Properties();

        config.setJdbcUrl(datasource.getDatabase());
        config.setUsername(datasource.getUsername());
        config.setPassword(datasource.getPassword());

        config.setAutoCommit(hikari.getAutoCommit());
        config.setAllowPoolSuspension(hikari.getAllowPoolSuspension());
        config.setMaximumPoolSize(hikari.getMaximumPoolSize());
        config.setMinimumIdle(hikari.getMinimumIdle());
        config.setIdleTimeout(hikari.getIdleTimeout());
        config.setMaxLifetime(hikari.getMaxLifetime());
        config.setConnectionTimeout(hikari.getConnectionTimeout());
        config.setPoolName(hikari.getPoolName());

        props.setProperty("cachePrepStmts", datasourceProperties.getCachePrepStmts());
        props.setProperty("prepStmtCacheSize", datasourceProperties.getPrepStmtCacheSize());
        props.setProperty("prepStmtCacheSqlLimit", datasourceProperties.getPrepStmtCacheSqlLimit());
        props.setProperty("useServerPrepStmts", datasourceProperties.getUseServerPrepStmts());
        props.setProperty("passwordCharacterEncoding", "UTF-8");
        props.setProperty("serverTimezone", "UTC");
        config.setDataSourceProperties(props);

        return new HikariDataSource(config);
    }

    @Primary
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
