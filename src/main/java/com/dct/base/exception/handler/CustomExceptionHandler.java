package com.dct.base.exception.handler;

import com.dct.base.common.BaseCommon;
import com.dct.base.constants.ExceptionConstants;
import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.dto.response.BaseResponseDTO;
import com.dct.base.exception.BaseAuthenticationException;
import com.dct.base.exception.BaseBadRequestException;
import com.dct.base.exception.BaseException;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        log.error("Handle method not allow exception. " + message);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.METHOD_NOT_ALLOWED,
            HttpStatusConstants.STATUS.FAILED,
            ExceptionConstants.METHOD_NOT_ALLOW,
            message
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  @Nullable HttpHeaders headers,
                                                                  @Nullable HttpStatusCode status,
                                                                  @Nullable WebRequest request) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String errorKey = ExceptionConstants.INVALID_DATA;

        if (Objects.nonNull(fieldError))
            errorKey = fieldError.getDefaultMessage();

        String reason = baseCommon.getMessageI18n(errorKey);
        log.error("Handle validate request data exception: {}. {}", reason, exception.getMessage());

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.BAD_REQUEST,
            HttpStatusConstants.STATUS.FAILED,
            errorKey
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ BaseAuthenticationException.class })
    public ResponseEntity<BaseResponseDTO> handleBaseAuthenticationException(BaseAuthenticationException exception) {
        String reason = baseCommon.getMessageI18n(exception.getErrorKey(), exception.getArgs());
        log.error("Handle authentication exception: {}", reason, exception);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.UNAUTHORIZED,
            HttpStatusConstants.STATUS.FAILED,
            exception.getErrorKey()
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ BaseBadRequestException.class })
    public ResponseEntity<BaseResponseDTO> handleBaseBadRequestException(BaseBadRequestException exception) {
        String reason = baseCommon.getMessageI18n(exception.getErrorKey(), exception.getArgs());
        log.error("Handle bad request alert exception: {}", reason, exception);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.BAD_REQUEST,
            HttpStatusConstants.STATUS.FAILED,
            exception.getErrorKey()
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ BaseException.class })
    public ResponseEntity<BaseResponseDTO> handleBaseException(BaseException exception) {
        String reason = baseCommon.getMessageI18n(exception.getErrorKey(), exception.getArgs());
        log.error("Handle exception: {}", reason, exception);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.BAD_REQUEST,
            HttpStatusConstants.STATUS.FAILED,
            exception.getErrorKey()
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<BaseResponseDTO> handleRuntimeException(RuntimeException exception) {
        log.error("Handle runtime exception", exception);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.BAD_REQUEST,
            HttpStatusConstants.STATUS.FAILED,
            ExceptionConstants.UNCERTAIN_ERROR
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<BaseResponseDTO> handleException(Exception exception) {
        log.error("Handle unexpected exception", exception);

        BaseResponseDTO responseDTO = new BaseResponseDTO(
            HttpStatusConstants.BAD_REQUEST,
            HttpStatusConstants.STATUS.FAILED,
            ExceptionConstants.UNCERTAIN_ERROR
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }
}
