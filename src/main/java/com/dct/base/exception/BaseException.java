package com.dct.base.exception;

import java.util.Arrays;

/**
 * Custom class for proactive exceptions in the application <p>
 *
 * {@link BaseException#entityName}: Name of the class that throws the exception <p>
 * {@link BaseException#errorKey}: The i18n message key used for language internationalization <p>
 * {@link BaseException#args}: Parameters accompanying the message of the `errorKey` if required
 *
 * @author thoaidc
 */
public class BaseException extends RuntimeException {

    private final String entityName;
    private final String errorKey;
    private final Object[] args;

    public BaseException(String entityName, String errorKey) {
        this(entityName, errorKey, null);
    }

    public BaseException(String entityName, String errorKey, Object[] args) {
        super(entityName + '-' + errorKey);
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

    @Override
    public String toString() {
        return "BaseException {" +
                "\nentityName: " + entityName +
                "\nerrorKey: " + errorKey +
                "\nargs: " + Arrays.toString(args) + "\n}";
    }
}
