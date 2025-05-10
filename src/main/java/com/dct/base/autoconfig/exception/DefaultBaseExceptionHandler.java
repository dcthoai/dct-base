package com.dct.base.autoconfig.exception;

import com.dct.base.core.exception.BaseExceptionHandler;
import com.dct.base.common.MessageTranslationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class DefaultBaseExceptionHandler extends BaseExceptionHandler {

    @Autowired
    public DefaultBaseExceptionHandler(MessageTranslationUtils messageUtils) {
        super(messageUtils);
    }
}
