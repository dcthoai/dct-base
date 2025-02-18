package com.dct.base.config.properties;

import com.dct.base.constants.PropertiesConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author thoaidc
 */
@Configuration
@ConfigurationProperties(prefix = PropertiesConstants.UPLOAD_RESOURCE_PROPERTIES)
public class UploadResourceConfig {

    private String uploadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
