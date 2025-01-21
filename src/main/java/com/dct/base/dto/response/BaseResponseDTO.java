package com.dct.base.dto.response;

import com.dct.base.constants.HttpStatusConstants;
import com.dct.base.constants.ResultConstants;

@SuppressWarnings("unused")
public class BaseResponseDTO {

    private Integer code;
    private Boolean status;
    private String message;
    private Object result;

    public static class Builder {
        private final BaseResponseDTO instance = new BaseResponseDTO();

        public Builder code(int code) {
            instance.code = code;
            return this;
        }

        public Builder success(boolean status) {
            instance.status = status;
            return this;
        }

        public Builder message(String message) {
            instance.message = message;
            return this;
        }

        public Builder result(Object result) {
            instance.result = result;
            return this;
        }

        public BaseResponseDTO build() {
            return instance;
        }
    }

    public BaseResponseDTO() {
        this.code = HttpStatusConstants.OK;
        this.status = HttpStatusConstants.STATUS.SUCCESS;
    }

    public BaseResponseDTO(Object result) {
        this.code = HttpStatusConstants.OK;
        this.status = HttpStatusConstants.STATUS.SUCCESS;
        this.message = ResultConstants.GET_DATA_SUCCESS;
        this.result = result;
    }

    public BaseResponseDTO(Integer code, Boolean status) {
        this.code = code;
        this.status = status;
    }

    public BaseResponseDTO(Integer code, Boolean status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public BaseResponseDTO(Integer code, Boolean status, String message, Object result) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
