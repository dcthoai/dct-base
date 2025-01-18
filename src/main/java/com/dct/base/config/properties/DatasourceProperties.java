package com.dct.base.config.properties;

import com.dct.base.constants.PropertiesConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = PropertiesConstants.DATASOURCE_PROPERTIES)
public class DatasourceProperties {

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
