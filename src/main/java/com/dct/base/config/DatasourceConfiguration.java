package com.dct.base.config;

import com.dct.base.config.properties.Datasource;
import com.dct.base.config.properties.DatasourceProperties;
import com.dct.base.config.properties.Hikari;
import com.dct.base.constants.ExceptionConstants;
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
public class DatasourceConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DatasourceConfiguration.class);
    private static final String ENTITY_NAME = "DatasourceConfiguration";

    public static class Builder {
        private Hikari hikari;
        private Datasource datasource;
        private DatasourceProperties datasourceProperties;

        public Builder withHikariConfig(Hikari hikariConfig) {
            this.hikari = hikariConfig;
            return this;
        }

        public Builder withDatasource(Datasource datasource) {
            this.datasource = datasource;
            return this;
        }

        public Builder withDatasourceProperties(DatasourceProperties datasourceProperties) {
            this.datasourceProperties = datasourceProperties;
            return this;
        }

        public DataSource build() {
            log.debug("Configuring datasource by default datasource builder");
            HikariConfig config = new HikariConfig();
            Properties props = new Properties();

            if (Objects.isNull(this.datasource)) {
                throw new BaseIllegalArgumentException(ENTITY_NAME, ExceptionConstants.DATASOURCE_CONFIG_INFO_NULL);
            }

            config.setDriverClassName(datasource.getDriverClassName());
            config.setJdbcUrl(datasource.getDatabase());
            config.setUsername(datasource.getUsername());
            config.setPassword(datasource.getPassword());

            if (Objects.nonNull(this.hikari)) {
                config.setAutoCommit(hikari.getAutoCommit());
                config.setAllowPoolSuspension(hikari.getAllowPoolSuspension());
                config.setMaximumPoolSize(hikari.getMaximumPoolSize());
                config.setMinimumIdle(hikari.getMinimumIdle());
                config.setIdleTimeout(hikari.getIdleTimeout());
                config.setMaxLifetime(hikari.getMaxLifetime());
                config.setConnectionTimeout(hikari.getConnectionTimeout());
                config.setPoolName(hikari.getPoolName());
            }

            if (Objects.nonNull(this.datasourceProperties)) {
                props.setProperty("cachePrepStmts", datasourceProperties.getCachePrepStmts());
                props.setProperty("prepStmtCacheSize", datasourceProperties.getPrepStmtCacheSize());
                props.setProperty("prepStmtCacheSqlLimit", datasourceProperties.getPrepStmtCacheSqlLimit());
                props.setProperty("useServerPrepStmts", datasourceProperties.getUseServerPrepStmts());
            }

            props.setProperty("passwordCharacterEncoding", StandardCharsets.UTF_8.name());
            props.setProperty("serverTimezone", "UTC"); // Uses the UTC standard for internationalized time
            config.setDataSourceProperties(props);

            return new HikariDataSource(config);
        }
    }
}
