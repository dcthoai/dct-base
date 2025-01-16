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

    // Runtime exception OR undetermined error
    String UNCERTAIN_ERROR = "exception.uncertain";
    String NULL_EXCEPTION = "exception.nullPointer";
    String FILTER_CHAIN_NOT_FOUND = "exception.filterChain.notFound";

    // Authentication exception
    String UNAUTHORIZED = "exception.auth.unauthorized";
    String FORBIDDEN = "exception.auth.forbidden";
    String TOKEN_INVALID_OR_EXPIRED = "exception.auth.token.invalidOrExpired";

    // Validate account info exception
    String REGISTER_FAILED = "exception.account.register.failed";
    String ACCOUNT_EXISTED = "exception.account.existed";
    String ACCOUNT_NOT_EXISTED = "exception.account.notExisted";
    String INVALID_DATA = "exception.account.data.invalid";
    String USERNAME_INVALID = "exception.account.username.invalid";
    String USERNAME_NOT_BLANK = "exception.account.username.notBlank";
    String PASSWORD_NOT_BLANK = "exception.account.password.notBlank";
    String PASSWORD_MIN_LENGTH = "exception.account.password.minLength";
    String PASSWORD_MAX_LENGTH = "exception.account.password.maxLength";
    String PASSWORD_INVALID = "exception.account.password.invalid";
    String PHONE_NOT_BLANK = "exception.account.phone.notBlank";
    String PHONE_INVALID = "exception.account.phone.invalid";
    String EMAIL_NOT_BANK = "exception.account.email.notBlank";
    String EMAIL_INVALID = "exception.account.email.invalid";
    String FULL_NAME_NOT_BLANK = "exception.account.fullName.notBlank";
    String FULL_NAME_INVALID = "exception.account.fullName.invalid";
    String ADDRESS_NOT_BLANK = "exception.account.address.notBlank";
    String ADDRESS_INVALID = "exception.account.address.invalid";
}
