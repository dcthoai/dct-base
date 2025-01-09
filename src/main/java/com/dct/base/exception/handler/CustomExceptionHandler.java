package com.dct.base.exception.handler;

import com.dct.base.common.BaseCommon;
import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.exception.BaseAuthenticationException;
import com.dct.base.exception.BaseException;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private final BaseCommon baseCommon;

    public CustomExceptionHandler(BaseCommon baseCommon) {
        this.baseCommon = baseCommon;
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(@Nullable HttpRequestMethodNotSupportedException ex,
                                                                      @Nullable HttpHeaders headers,
                                                                      @Nullable HttpStatusCode status,
                                                                      @Nullable WebRequest request) {
        String message = Objects.nonNull(ex) ? ex.getMessage() : "";
        log.debug("Handle method not allow exception. " + message);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.METHOD_NOT_ALLOWED,
            HttpStatusConstants.STATUS.FAILED,
            ExceptionConstants.METHOD_NOT_ALLOW,
            message
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({ BaseException.class })
    public ResponseEntity<BaseResponseDTO> handleBaseException(BaseException exception) {
        log.debug("Handle base exception", exception);
        String reason = baseCommon.getMessageI18n(exception.getErrorKey(), exception.getArgs());
        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.BAD_REQUEST,
            HttpStatusConstants.STATUS.FAILED,
            reason,
            exception.getMessage()
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ BaseAuthenticationException.class })
    public ResponseEntity<BaseResponseDTO> handleBaseAuthenticationException(BaseAuthenticationException exception) {
        log.debug("Handle authentication exception", exception);
        String reason = baseCommon.getMessageI18n(exception.getErrorKey(), exception.getArgs());
        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.UNAUTHORIZED,
            HttpStatusConstants.STATUS.FAILED,
            reason,
            exception.getMessage()
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<BaseResponseDTO> handleException(Exception exception) {
        log.debug("Handle exception", exception);
        String reason = baseCommon.getMessageI18n(exception.getMessage());
        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.BAD_REQUEST,
            HttpStatusConstants.STATUS.FAILED,
            reason,
            exception.getMessage()
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }
}
