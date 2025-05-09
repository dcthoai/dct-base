package com.dct.base.config;

import com.dct.base.config.properties.BaseDatasourceConnectionProperties;
import com.dct.base.config.properties.BaseDatasourceProperties;
import com.dct.base.config.properties.BaseHikariProperties;
import com.dct.base.constants.BaseExceptionConstants;
import com.dct.base.exception.BaseIllegalArgumentException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/**
 * @author thoaidc
 */
@SuppressWarnings("unused")
public class BaseDataSourceConfigurer {

    private static final Logger log = LoggerFactory.getLogger(BaseDataSourceConfigurer.class);
    private static final String ENTITY_NAME = "BaseDataSourceConfigurer";

    public static class Builder {
        private BaseHikariProperties hikariProperties;
        private BaseDatasourceConnectionProperties datasourceConnectionProperties;
        private BaseDatasourceProperties datasourceProperties;

        public Builder withHikariConfig(BaseHikariProperties hikariProperties) {
            this.hikariProperties = hikariProperties;
            return this;
        }

        public Builder withDatasource(BaseDatasourceConnectionProperties datasourceConnectionProperties) {
            this.datasourceConnectionProperties = datasourceConnectionProperties;
            return this;
        }

        public Builder withDatasourceProperties(BaseDatasourceProperties datasourceProperties) {
            this.datasourceProperties = datasourceProperties;
            return this;
        }

        public DataSource build() {
            log.debug("Configuring datasource by default datasource builder");
            HikariConfig hikariConfig = new HikariConfig();
            Properties properties = new Properties();

            if (Objects.isNull(this.datasourceConnectionProperties)) {
                throw new BaseIllegalArgumentException(ENTITY_NAME, BaseExceptionConstants.DATASOURCE_CONFIG_INFO_NULL);
            }

            hikariConfig.setDriverClassName(datasourceConnectionProperties.getDriverClassName());
            hikariConfig.setJdbcUrl(datasourceConnectionProperties.getDatabase());
            hikariConfig.setUsername(datasourceConnectionProperties.getUsername());
            hikariConfig.setPassword(datasourceConnectionProperties.getPassword());

            if (Objects.nonNull(this.hikariProperties)) {
                hikariConfig.setAutoCommit(hikariProperties.getAutoCommit());
                hikariConfig.setAllowPoolSuspension(hikariProperties.getAllowPoolSuspension());
                hikariConfig.setMaximumPoolSize(hikariProperties.getMaximumPoolSize());
                hikariConfig.setMinimumIdle(hikariProperties.getMinimumIdle());
                hikariConfig.setIdleTimeout(hikariProperties.getIdleTimeout());
                hikariConfig.setMaxLifetime(hikariProperties.getMaxLifetime());
                hikariConfig.setConnectionTimeout(hikariProperties.getConnectionTimeout());
                hikariConfig.setPoolName(hikariProperties.getPoolName());
            }

            if (Objects.nonNull(this.datasourceProperties)) {
                properties.setProperty("cachePrepStmts", datasourceProperties.getCachePrepStmts());
                properties.setProperty("prepStmtCacheSize", datasourceProperties.getPrepStmtCacheSize());
                properties.setProperty("prepStmtCacheSqlLimit", datasourceProperties.getPrepStmtCacheSqlLimit());
                properties.setProperty("useServerPrepStmts", datasourceProperties.getUseServerPrepStmts());
            }

            properties.setProperty("passwordCharacterEncoding", StandardCharsets.UTF_8.name());
            properties.setProperty("serverTimezone", "UTC"); // Uses the UTC standard for internationalized time
            hikariConfig.setDataSourceProperties(properties);

            return new HikariDataSource(hikariConfig);
        }
    }
}
