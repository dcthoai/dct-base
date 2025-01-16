package com.dct.base.exception;

import com.dct.base.constants.ExceptionConstants;
import org.springframework.security.core.AuthenticationException;

public class BaseAuthenticationException extends AuthenticationException {

    private final String errorKey;
    private final String entityName;
    private final Object[] args;
    private boolean isDefault = false;

    public BaseAuthenticationException(String entityName, String errorKey, Object ...args) {
        super(errorKey);
        this.errorKey = errorKey;
        this.entityName = entityName;
        this.args = args;
    }

    public BaseAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
        this.args = null;
        this.errorKey = ExceptionConstants.UNAUTHORIZED;
        this.entityName = this.getEntityName();
        this.isDefault = true;
    }

    public BaseAuthenticationException(String entityName, String errorKey, String msg, Throwable cause, Object ...args) {
        super(msg, cause);
        this.errorKey = errorKey;
        this.entityName = entityName;
        this.args = args;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String getMessage() {
        if (isDefault)
            return super.getMessage();

        return errorKey;
    }
}
