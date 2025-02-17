package com.dct.base.constants;

/**
 * Messages for exceptions with internationalization (I18n) here<p>
 * The constant content corresponds to the message key in the resources bundle files in directories such as:
 * <ul>
 *   <li><a href="">resources/common/i18n/messages</a></li>
 *   <li><a href="">resources/i18n/messages</a></li>
 * </ul>
 * These paths are defined in {@link BaseConstants#MESSAGE_SOURCE_BASENAME}
 *
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

    // Authentication exception
    String BAD_CREDENTIALS = "exception.auth.badCredentials";
    String ACCOUNT_EXPIRED = "exception.auth.accountExpired";
    String ACCOUNT_NOT_FOUND = "exception.auth.accountNotFound";
    String UNAUTHORIZED = "exception.auth.unauthorized";
    String FORBIDDEN = "exception.auth.forbidden";
    String TOKEN_INVALID_OR_EXPIRED = "exception.auth.token.invalidOrExpired";

    // OAuth2 authorization exception
    String OAUTH2_AUTHORIZATION_CODE_NOT_FOUND = "exception.oauth2.authorizationCode.notFound";
    String OAUTH2_AUTHORIZATION_CODE_CLIENT_ERROR = "exception.oauth2.authorizationCode.clientError";
    String OAUTH2_AUTHORIZATION_CODE_SERVER_ERROR = "exception.oauth2.authorizationCode.serverError";
    String OAUTH2_AUTHORIZATION_CODE_EXCEPTION = "exception.oauth2.authorizationCode.exception";
    String OAUTH2_USER_INFO_NOT_FOUND = "exception.oauth2.userinfo.notFound";
    String OAUTH2_USER_INFO_CLIENT_ERROR = "exception.oauth2.userinfo.clientError";
    String OAUTH2_USER_INFO_SERVER_ERROR = "exception.oauth2.userinfo.serverError";
    String OAUTH2_USER_INFO_EXCEPTION = "exception.oauth2.userinfo.exception";

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

    // Upload file request
    String MAXIMUM_UPLOAD_SIZE_EXCEEDED = "exception.upload.maximumSizeExceed";
}
