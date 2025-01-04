package com.dct.base.constants;

import com.dct.base.dto.NotifyConfigMap;
import com.dct.base.entity.SystemConfig;

/**
 * <h3>Includes all configuration constants for the notification configuration.</h3>
 *
 * <p>The {@link CONFIG CONFIG} section contains</p>
 * <ul>
 *   <li>{@link CONFIG#SYSTEM_CODE SYSTEM_CODE}, which corresponds to {@link SystemConfig} code stored in database</li>
 *   <li>
 *       {@link CONFIG#JSON_CONFIG_FILE JSON_CONFIG_FILE}, which is the path to the config file on the server
 *       <p>See sample details config in <a href="/mnt/shared_file/config/notifyConfig.json">notifyConfig.json</a></p>
 *   </li>
 * </ul>
 *
 * <p>
 *   The {@link FUNCTION FUNCTION} section stores enums, with each enum containing a set of codes
 *   corresponding to the notification configurations used within that specific function.
 *   They are used with {@link NotifyConfigMap NotifyConfigMap}
 * </p>
 *
 * @author thoaidc
 */
public interface NotifyConfigConstants {

    interface CONFIG {
        String SYSTEM_CODE = "notify_config";
        String JSON_CONFIG_FILE = "mnt/shared_file/config/notifyConfig.json";
    }

    interface FUNCTION {

    }
}
