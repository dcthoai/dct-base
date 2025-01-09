package com.dct.base.exception;

@SuppressWarnings("unused")
public class BaseException extends RuntimeException {

    private final String entityName;
    private final String errorKey;
    private final Object[] args;

    public BaseException(String entityName, String errorKey) {
        this(entityName, errorKey, null);
    }

    public BaseException(String entityName, String errorKey, Object[] args) {
        super();
        this.entityName = entityName;
        this.errorKey = errorKey;
        this.args = args;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public Object[] getArgs() {
        return args;
    }
}
