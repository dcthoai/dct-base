package com.dct.base.interceptor;

import com.dct.base.common.BaseCommon;
import com.dct.base.dto.response.BaseResponseDTO;
import jakarta.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

@ControllerAdvice
public class BaseResponseFilter implements ResponseBodyAdvice<Object> {

    private static final Logger log = LoggerFactory.getLogger(BaseResponseFilter.class);
    private final BaseCommon baseCommon;

    public BaseResponseFilter(BaseCommon baseCommon) {
        this.baseCommon = baseCommon;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        boolean isBaseResponseDTOType = BaseResponseDTO.class.isAssignableFrom(returnType.getParameterType());
        boolean isBasicResponseEntity = ResponseEntity.class.isAssignableFrom(returnType.getParameterType());
        log.debug("Response will be processed by: " + converterType.getSimpleName());

        return isBaseResponseDTOType || isBasicResponseEntity;
    }

    /**
     * @param body Contents of the HTTP response being processed
     * @param returnType Contains information about the return data type of the method that Spring is processing
     * @param selectedContentType Media type requested by the client, usually application/json, application/xml, etc
     * @param selectedConverterType Type of converter that Spring will use to convert the response data (to JSON, XML,...)
     * @param request ServerHttpRequest
     * @param response ServerHttpResponse
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  @Nullable MediaType selectedContentType,
                                  @Nullable Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @Nullable ServerHttpRequest request,
                                  @Nullable ServerHttpResponse response) {
        Class<?> responseType = returnType.getParameterType();

        if (Objects.equals(responseType, BaseResponseDTO.class)) {
            return baseCommon.setResponseMessageI18n((BaseResponseDTO) body);
        }

        if (Objects.equals(responseType, ResponseEntity.class)) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) body;
            Object responseBody = responseEntity.getBody();

            if (responseBody instanceof BaseResponseDTO) {
                BaseResponseDTO responseDTO = baseCommon.setResponseMessageI18n((BaseResponseDTO) responseBody);
                return new ResponseEntity<>(responseDTO, responseEntity.getHeaders(), responseEntity.getStatusCode());
            }
        }

        return body;
    }
}
