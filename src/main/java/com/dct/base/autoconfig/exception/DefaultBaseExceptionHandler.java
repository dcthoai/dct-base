package com.dct.base.autoconfig.exception;

import com.dct.base.core.exception.BaseExceptionHandler;
import com.dct.base.common.MessageTranslationUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@ConditionalOnMissingBean(value = BaseExceptionHandler.class)
public class DefaultBaseExceptionHandler extends BaseExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultBaseExceptionHandler.class);

    @Autowired
    public DefaultBaseExceptionHandler(MessageTranslationUtils messageTranslationUtils) {
        super(messageTranslationUtils);
        log.debug("`DefaultBaseExceptionHandler` has been automatically configured to handle exceptions");
    }
}
