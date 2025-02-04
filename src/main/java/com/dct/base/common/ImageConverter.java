package com.dct.base.common;

import com.dct.base.constants.BaseConstants;
import com.dct.base.dto.ImageDTO;
import com.dct.base.dto.ImageParameterDTO;
import com.dct.base.exception.BaseBadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@SuppressWarnings("unused")
public class ImageConverter {

    private static final Logger log = LoggerFactory.getLogger(ImageConverter.class);
    private static final String ENTITY_NAME = "ImageConverter";

    public static boolean isCompressibleImage(MultipartFile file) {
        return isValidImageFormat(file, BaseConstants.UPLOAD_RESOURCES.COMPRESSIBLE_IMAGE_FORMATS);
    }

    public static boolean isValidImageFormat(MultipartFile file) {
        return isValidImageFormat(file, BaseConstants.UPLOAD_RESOURCES.VALID_IMAGE_FORMATS);
    }

    public static boolean isValidImageFormat(MultipartFile file, String[] fileTypes) {
        if (FileUtils.isInvalidUploadFile(file))
            return false;

        String lowerCaseFileName = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase();

        for (String format : fileTypes) {
            if (lowerCaseFileName.endsWith(format)) {
                return true;
            }
        }

        return false;
    }

    public static ImageDTO compressImage(MultipartFile image) throws IOException {
        if (!isCompressibleImage(image))
            throw new BaseBadRequestException(ENTITY_NAME, "Image format does not support compression!");

        ImageParameterDTO imageParameterDTO = getImageCompressionFactor(image);

        if (imageParameterDTO.isCompressed())
            throw new BaseBadRequestException(ENTITY_NAME, "Image cannot be compressed further!");

        int originalImageWidth = imageParameterDTO.getOriginalImageWidth();
        int originalImageHeight = imageParameterDTO.getOriginalImageHeight();
        int newImageWidth = (int) (originalImageWidth * imageParameterDTO.getSizeCompressionFactor());
        int newImageHeight = (int) (originalImageHeight * imageParameterDTO.getSizeCompressionFactor());

        BufferedImage bufferedImage = imageParameterDTO.getBufferedImage();
        BufferedImage resizedImage = new BufferedImage(newImageWidth, newImageHeight, bufferedImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bufferedImage, 0, 0, newImageWidth, newImageHeight, null);
        g.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
        IIOImage iioImage = new IIOImage(resizedImage, null, null);
        String imageExtension = imageParameterDTO.getImageFileExtension().substring(1);
        ImageWriter imageWriter = getImageWriter(imageExtension);
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(imageParameterDTO.getQualityCompressionFactor());

        imageWriter.setOutput(imageOutputStream);
        imageWriter.write(null, iioImage, imageWriteParam);
        imageWriter.dispose();

        return new ImageDTO(byteArrayOutputStream, imageParameterDTO);
    }

    public static ImageParameterDTO getImageCompressionFactor(MultipartFile image) {
        ImageParameterDTO imageParameterDTO = getImageParameter(image);

        long imageFileSize = imageParameterDTO.getOriginalImageFileSize();
        int imageWidth = imageParameterDTO.getOriginalImageWidth();
        int imageHeight = imageParameterDTO.getOriginalImageHeight();

        imageParameterDTO.setSizeCompressionFactor(getImageSizeCompressionFactor(imageWidth, imageHeight));
        imageParameterDTO.setQualityCompressionFactor(getImageQualityCompressionFactor(imageFileSize));

        return imageParameterDTO;
    }

    public static ImageParameterDTO getImageParameter(MultipartFile image) {
        ImageParameterDTO imageParameter = new ImageParameterDTO();
        BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(image.getInputStream());

            if (bufferedImage == null) {
                log.error("Could not read image: {}. Invalid format!", image.getOriginalFilename());
                return imageParameter;
            }
        } catch (IOException e) {
            log.error("Could not read image: {}. I/O error!", image.getOriginalFilename());
            return imageParameter;
        }

        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();
        long imageFileSize = image.getSize();
        String imageFileName = Optional.ofNullable(image.getOriginalFilename()).orElse("");
        String defaultImageFormat = BaseConstants.UPLOAD_RESOURCES.DEFAULT_IMAGE_FORMAT;
        String imageFileExtension = "";

        if (!imageFileName.isBlank() && imageFileName.contains("."))
            imageFileExtension = imageFileName.substring(imageFileName.lastIndexOf('.')).toLowerCase();

        imageParameter.setBufferedImage(bufferedImage);
        imageParameter.setOriginalImageWidth(imageWidth);
        imageParameter.setOriginalImageHeight(imageHeight);
        imageParameter.setOriginalImageFileSize(imageFileSize);
        imageParameter.setOriginalImageFilename(image.getOriginalFilename());
        imageParameter.setImageFileExtension(imageFileExtension.length() > 1 ? imageFileExtension : defaultImageFormat);

        return imageParameter;
    }

    private static float getImageQualityCompressionFactor(long originalImageFileSize) {
        float qualityCompressionFactor = 1.0f; // Default keep image quality

        if (originalImageFileSize > 5 * 1024 * 1024) { // > 5MB
            qualityCompressionFactor = 0.5f;
        } else if (originalImageFileSize > 2 * 1024 * 1024) { // 2MB - 5MB
            qualityCompressionFactor = 0.6f;
        } else if (originalImageFileSize > 500 * 1024) { // 500KB - 2MB
            qualityCompressionFactor = 0.8f;
        } else if (originalImageFileSize > 300 * 1024) { // 300KB - 500KB
            qualityCompressionFactor = 0.9f;
        }

        return qualityCompressionFactor;
    }

    private static float getImageSizeCompressionFactor(int originalImageWidth, int originalImageHeight) {
        float sizeCompressionFactor = 1.0f; // Default keep image size

        if (originalImageWidth > 4000 || originalImageHeight > 4000) {
            sizeCompressionFactor = 0.5f; // Resize 50%
        } else if (originalImageWidth > 2000 || originalImageHeight > 2000) {
            sizeCompressionFactor = 0.7f; // Resize 70%
        } else if (originalImageWidth > 1200 || originalImageHeight > 1080) {
            sizeCompressionFactor = 0.85f;
        }

        return sizeCompressionFactor;
    }

    // Phương thức hỗ trợ để lấy ImageWriter cho định dạng cụ thể
    private static ImageWriter getImageWriter(String format) {
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(format);

        if (!imageWriters.hasNext()) {
            throw new IllegalStateException("Not found ImageWriter for: " + format);
        }

        return imageWriters.next();
    }
}
