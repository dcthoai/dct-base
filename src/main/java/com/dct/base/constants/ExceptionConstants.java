package com.dct.base.constants;

/**
 * Messages for exceptions with internationalization (I18n) here<p>
 * The constant content corresponds to the message key in the resources bundle files of the resources/i18n directory
 * @author thoaidc
 */
public interface ExceptionConstants {

    // I18n exception
    String TRANSLATE_NOT_FOUND = "exception.i18n.notFound";

    // Http exception
    String METHOD_NOT_ALLOW = "exception.http.methodNotAllow";

    // Authentication exception
    String UNAUTHORIZED = "exception.auth.unauthorized";
    String FORBIDDEN = "exception.auth.forbidden";
}
