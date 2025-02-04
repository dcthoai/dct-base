package com.dct.base.dto;

import java.awt.image.BufferedImage;

public class ImageParameterDTO {

    private int originalImageWidth;
    private int originalImageHeight;
    private long originalImageFileSize;
    private String originalImageFilename;
    private String imageFileExtension;
    private BufferedImage bufferedImage;
    private float sizeCompressionFactor = 1.0f;
    private float qualityCompressionFactor = 1.0f;

    public boolean isCompressed() {
        return sizeCompressionFactor >= 1.0f && qualityCompressionFactor >= 1.0f;
    }

    public int getOriginalImageWidth() {
        return originalImageWidth;
    }

    public void setOriginalImageWidth(int originalImageWidth) {
        this.originalImageWidth = originalImageWidth;
    }

    public int getOriginalImageHeight() {
        return originalImageHeight;
    }

    public void setOriginalImageHeight(int originalImageHeight) {
        this.originalImageHeight = originalImageHeight;
    }

    public long getOriginalImageFileSize() {
        return originalImageFileSize;
    }

    public void setOriginalImageFileSize(long originalImageFileSize) {
        this.originalImageFileSize = originalImageFileSize;
    }

    public String getOriginalImageFilename() {
        return originalImageFilename;
    }

    public void setOriginalImageFilename(String originalImageFilename) {
        this.originalImageFilename = originalImageFilename;
    }

    public String getImageFileExtension() {
        return imageFileExtension;
    }

    public void setImageFileExtension(String imageFileExtension) {
        this.imageFileExtension = imageFileExtension;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public float getSizeCompressionFactor() {
        return sizeCompressionFactor;
    }

    public void setSizeCompressionFactor(float sizeCompressionFactor) {
        this.sizeCompressionFactor = sizeCompressionFactor;
    }

    public float getQualityCompressionFactor() {
        return qualityCompressionFactor;
    }

    public void setQualityCompressionFactor(float qualityCompressionFactor) {
        this.qualityCompressionFactor = qualityCompressionFactor;
    }
}
