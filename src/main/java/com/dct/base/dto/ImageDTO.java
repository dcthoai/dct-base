package com.dct.base.dto;

import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class ImageDTO {

    private ByteArrayOutputStream byteArrayOutputStream;
    private ImageParameterDTO imageParameterDTO;

    public ImageDTO(ByteArrayOutputStream byteArrayOutputStream, ImageParameterDTO imageParameterDTO) {
        this.byteArrayOutputStream = byteArrayOutputStream;
        this.imageParameterDTO = imageParameterDTO;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public ImageParameterDTO getImageParameterDTO() {
        return imageParameterDTO;
    }

    public void setImageParameterDTO(ImageParameterDTO imageParameterDTO) {
        this.imageParameterDTO = imageParameterDTO;
    }
}
