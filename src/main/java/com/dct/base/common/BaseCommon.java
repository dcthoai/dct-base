package com.dct.base.common;

import com.dct.base.constants.ExceptionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Provide common processing functions for the entire application
 * @author thoaidc
 */
@Component
public class BaseCommon {

    private static final Logger log = LoggerFactory.getLogger(BaseCommon.class);
    private final MessageSource messageSource; // Spring boot service for I18n

    public BaseCommon(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get the internationalized content (I18n) of the key based on the current locale in the application
     * @param messageKey The code corresponding to the internationalized content to be retrieved
     * @param args Arguments passed to use dynamic values for message
     * @return Value of {@link ExceptionConstants#TRANSLATE_NOT_FOUND} if not found message I18n
     */
    public String getMessageI18n(String messageKey, Object ...args) {
        log.info("Translate message for {}", messageKey);
        // The value of Locale represents the current region, here used to determine the language type to translate
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, args, ExceptionConstants.TRANSLATE_NOT_FOUND, locale);
    }
}
