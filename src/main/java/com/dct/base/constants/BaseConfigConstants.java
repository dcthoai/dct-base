package com.dct.base.constants;

/**
 * Contains the common configuration constants for the project without security configurations
 * @author thoaidc
 */
@SuppressWarnings("unused")
public interface BaseConfigConstants {

    String ANONYMOUS_USER = "anonymousUser"; // The default user when not authenticated in Spring Security
    String DEFAULT_CREATOR = "system"; // Used instead of the default user value mentioned above to store in the database
    String MESSAGE_SOURCE_ENCODING = "UTF-8"; // Specifies the charset for i18n messages

    // The location where the resource bundle files for i18n messages are stored
    String[] MESSAGE_SOURCE_BASENAME = { "classpath:i18n/messages" };

    interface UPLOAD_RESOURCES {
        String PREFIX_PATH = "/uploads/";
        String DEFAULT_UPLOAD_PATH = "/uploads/";
        String[] VALID_IMAGE_FORMATS = { ".png", ".jpg", ".jpeg", ".gif", ".svg", ".webp", ".webm" };
        String[] COMPRESSIBLE_IMAGE_FORMATS = { ".png", ".jpg", ".jpeg", ".webp" };
        String DEFAULT_IMAGE_FORMAT = ".webp";
        String DEFAULT_IMAGE_PATH_FOR_ERROR = PREFIX_PATH + "error/error_image.webp";
        String PNG = "png";
        String WEBP = "webp";
        String JPG = "jpg";
        String JPEG = "jpeg";
    }
}
