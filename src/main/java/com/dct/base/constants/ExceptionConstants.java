package com.dct.base.constants;

/**
 * Messages for exceptions with internationalization (I18n) here<p>
 * The constant content corresponds to the message key in the resources bundle files of the resources/i18n directory
 * @author thoaidc
 */
@SuppressWarnings("unused")
public interface ExceptionConstants {

    // I18n exception
    String TRANSLATE_NOT_FOUND = "exception.i18n.notFound";

    // Http exception
    String METHOD_NOT_ALLOW = "exception.http.methodNotAllow";

    // Authentication exception
    String UNAUTHORIZED = "exception.auth.unauthorized";
    String FORBIDDEN = "exception.auth.forbidden";

    // Validate account info exception
    String INVALID_DATA = "account.data.invalid";
    String USERNAME_INVALID = "account.username.invalid";
    String USERNAME_NOT_BLANK = "account.username.notBlank";
    String PASSWORD_NOT_BLANK = "account.password.notBlank";
    String PASSWORD_MIN_LENGTH = "account.password.minLength";
    String PASSWORD_MAX_LENGTH = "account.password.maxLength";
    String PASSWORD_INVALID = "account.password.invalid";
    String PHONE_NOT_BLANK = "account.phone.notBlank";
    String PHONE_INVALID = "account.phone.invalid";
    String EMAIL_NOT_BANK = "account.email.notBlank";
    String EMAIL_INVALID = "account.email.invalid";
    String FULL_NAME_NOT_BLANK = "account.fullName.notBlank";
    String FULL_NAME_INVALID = "account.fullName.invalid";
    String ADDRESS_NOT_BLANK = "account.address.notBlank";
    String ADDRESS_INVALID = "account.address.invalid";
}
