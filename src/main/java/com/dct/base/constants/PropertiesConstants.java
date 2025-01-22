package com.dct.base.constants;

/**
 * Contains the prefixes for the config property files <p>
 * Refer to these files in the <a href="">com/dct/base/config/properties</a> directory for more details
 *
 * @author thoaidc
 */
public interface PropertiesConstants {

    String DATASOURCE = "dct-base.datasource";
    String DATASOURCE_PROPERTIES = "dct-base.datasource.hikari.datasource-properties";
    String HIKARI = "dct-base.datasource.hikari";
    String JPA_PROPERTIES = "dct-base.jpa";
    String SECURITY_CONFIG = "dct-base.security.auth";
    String OAUTH2_ACTIVE_STATUS = "dct-base.security.oauth2.enabled";
    String GOOGLE_OAUTH2_PROPERTIES = "dct-base.security.oauth2.google";
}
