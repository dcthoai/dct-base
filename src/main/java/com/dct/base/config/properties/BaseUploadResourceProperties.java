package com.dct.base.config.properties;

import com.dct.base.constants.BasePropertiesConstants;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author thoaidc
 */
@Component
@ConditionalOnProperty(value = BasePropertiesConstants.UPLOAD_RESOURCES)
@ConfigurationProperties(prefix = BasePropertiesConstants.UPLOAD_RESOURCES, ignoreUnknownFields = false)
public class BaseUploadResourceProperties {

    private String uploadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
