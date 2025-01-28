package com.dct.base.dto.request;

/**
 * Contains common information for all requests
 * @author thoaidc
 */
@SuppressWarnings("unused")
public class BaseRequestDTO {

    private String deviceID;

    public BaseRequestDTO() {}

    public BaseRequestDTO(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
