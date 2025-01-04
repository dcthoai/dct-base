package com.dct.base.dto.response;

import com.dct.base.constants.HttpStatusConstants;

@SuppressWarnings("unused")
public class BaseResponseDTO {

    private Integer code;
    private Boolean status;
    private String message;
    private Object result;

    public BaseResponseDTO() {
        this.code = HttpStatusConstants.OK;
        this.status = HttpStatusConstants.STATUS.SUCCESS;
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
