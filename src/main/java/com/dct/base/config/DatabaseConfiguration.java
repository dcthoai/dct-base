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
        properties.setProperty("passwordCharacterEncoding", "UTF-8");

        config.setDataSourceClassName(env.getProperty("spring.datasource.type"));
        config.setUsername( env.getProperty("spring.datasource.username"));
        config.setPassword(env.getProperty("spring.datasource.password"));
        config.setAutoCommit(false);
        config.setAllowPoolSuspension(false);
        config.setPoolName("HikariPool");
        config.setDataSourceProperties(properties);

        return new HikariDataSource(config);
    }

    @Primary
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
