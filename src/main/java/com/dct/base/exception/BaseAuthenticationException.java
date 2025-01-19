package com.dct.base.exception;

public class BaseAuthenticationException extends BaseException {

    public BaseAuthenticationException(String entityName, String errorKey) {
        super(entityName, errorKey);
    }

    public BaseAuthenticationException(String entityName, String errorKey, Object[] args) {
        super(entityName, errorKey, args);
    }
}
