package com.dct.base.config.properties;

import com.dct.base.constants.BasePropertiesConstants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Contains configuration properties related to optimizing database connections<p>
 * When the application starts, Spring will automatically create an instance of this class
 * and load the values from configuration files like application.properties or application.yml <p>
 *
 * {@link ConfigurationProperties} helps Spring map config properties to fields,
 * instead of using @{@link Value} for each property individually <p>
 *
 * {@link BasePropertiesConstants#DATASOURCE_PROPERTIES} decides the prefix for the configurations that will be mapped <p>
 *
 * See <a href="">application-dev.yml</a> for detail
 *
 * @author thoaidc
 */
@Component
@ConditionalOnProperty(value = BasePropertiesConstants.DATASOURCE_PROPERTIES)
@ConfigurationProperties(prefix = BasePropertiesConstants.DATASOURCE_PROPERTIES, ignoreUnknownFields = false)
public class BaseDatasourceProperties {

    private String cachePrepStmts;
    private String prepStmtCacheSize;
    private String prepStmtCacheSqlLimit;
    private String useServerPrepStmts;

    public String getCachePrepStmts() {
        return cachePrepStmts;
    }

    public void setCachePrepStmts(String cachePrepStmts) {
        this.cachePrepStmts = cachePrepStmts;
    }

    public String getPrepStmtCacheSize() {
        return prepStmtCacheSize;
    }

    public void setPrepStmtCacheSize(String prepStmtCacheSize) {
        this.prepStmtCacheSize = prepStmtCacheSize;
    }

    public String getPrepStmtCacheSqlLimit() {
        return prepStmtCacheSqlLimit;
    }

    public void setPrepStmtCacheSqlLimit(String prepStmtCacheSqlLimit) {
        this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
    }

    public String getUseServerPrepStmts() {
        return useServerPrepStmts;
    }

    public void setUseServerPrepStmts(String useServerPrepStmts) {
        this.useServerPrepStmts = useServerPrepStmts;
    }
}
