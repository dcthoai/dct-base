package com.dct.base.exception;

public class BaseBadRequestException extends BaseException {

    public BaseBadRequestException(String entityName, String errorKey) {
        super(entityName, errorKey);
    }

    public BaseBadRequestException(String entityName, String errorKey, Object[] args) {
        super(entityName, errorKey, args);
    }
}
