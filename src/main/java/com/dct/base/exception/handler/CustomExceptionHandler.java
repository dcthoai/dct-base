package com.dct.base.exception.handler;

import com.dct.base.common.BaseCommon;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.exception.BaseAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private final BaseCommon baseCommon;

    public CustomExceptionHandler(BaseCommon baseCommon) {
        this.baseCommon = baseCommon;
    }

    @ExceptionHandler({ BaseAuthenticationException.class })
    public ResponseEntity<Object> handleBaseAuthenticationException(BaseAuthenticationException exception) {
        log.debug("Handle authentication exception", exception);
        String reason = baseCommon.getMessageI18n(exception.getErrorKey(), exception.getArgs());
        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.UNAUTHORIZED,
            HttpStatusConstants.STATUS.FAILED,
            reason
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleException(Exception exception) {
        log.debug("Handle exception", exception);
        String reason = baseCommon.getMessageI18n(exception.getMessage());
        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.UNPROCESSABLE_ENTITY,
            HttpStatusConstants.STATUS.FAILED,
            reason
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.EXPECTATION_FAILED);
    }
}
