package com.dct.base.constants;

/**
 * Contains the prefixes for the config property files <p>
 * Refer to these files in the <a href="">com/dct/nextgen/config/properties</a> directory for more details
 *
 * @author thoaidc
 */
public interface PropertiesConstants {

    String DATASOURCE = "dct-base.datasource.connection";
    String DATASOURCE_PROPERTIES = "dct-base.datasource.properties";
    String HIKARI = "dct-base.datasource.hikari";
    String JPA_PROPERTIES = "dct-base.jpa";
    String JPA_BASE_REPOSITORY_PACKAGES = "dct-base.jpa.base-repositories-packages";
    String SECURITY_CONFIG = "dct-base.security.auth";
    String UPLOAD_RESOURCE_PROPERTIES = "dct-base.resources.upload";
    String RABBIT_MQ_PROPERTIES = "spring.rabbitmq";
}
