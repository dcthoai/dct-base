package com.dct.base.common;

import com.dct.base.config.properties.BaseUploadResourceProperties;
import com.dct.base.constants.BaseConfigConstants;
import com.dct.base.dto.upload.ImageDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author thoaidc
 */
@Service
@SuppressWarnings("unused")
public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);
    private static final String ENTITY_NAME = "FileUtils";
    private final String UPLOAD_PATH;

    public FileUtils(@Qualifier("baseUploadResourceProperties") @Nullable BaseUploadResourceProperties uploadResourceConfig) {
        if (Objects.nonNull(uploadResourceConfig)) {
            UPLOAD_PATH = uploadResourceConfig.getUploadPath();
        } else {
            UPLOAD_PATH = BaseConfigConstants.UPLOAD_RESOURCES.DEFAULT_UPLOAD_PATH;
        }
    }

    public static boolean invalidUploadFile(MultipartFile file) {
        return file == null || file.isEmpty() || !Objects.nonNull(file.getOriginalFilename());
    }

    public static boolean invalidUploadFiles(MultipartFile[] files) {
        if (files == null || files.length == 0)
            return true;

        for (MultipartFile file : files) {
            if (invalidUploadFile(file))
                return true;
        }

        return false;
    }

    private File getFileToSave(String fileName, boolean isMakeNew) {
        File file = new File(UPLOAD_PATH + File.separator + fileName);

        if (file.exists() || !isMakeNew)
            return file;

        try {
            // Make sure the parent directory exists
            File parentDir = file.getParentFile();

            if (Objects.nonNull(parentDir) && !parentDir.exists() && !parentDir.mkdirs()) {
                log.warn("[{}] - Could not create parent directory: {}", ENTITY_NAME, parentDir.getAbsolutePath());
                return null;
            }

            return file.createNewFile() ? file : null;
        } catch (Exception e) {
            log.warn("[{}] - Could not create new file at: {}", ENTITY_NAME, file.getAbsolutePath());
        }

        return null;
    }

    public static String generateUniqueFileName(String fileNameOrFileExtension) {
        String uniqueName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_SSS"));

        if (Objects.isNull(fileNameOrFileExtension))
            return uniqueName + BaseConfigConstants.UPLOAD_RESOURCES.DEFAULT_IMAGE_FORMAT;

        String fileExtension = fileNameOrFileExtension.substring(fileNameOrFileExtension.lastIndexOf("."));
        return uniqueName + fileExtension;
    }

    public String save(ImageDTO imageDTO) {
        try {
            String fileName = generateUniqueFileName(imageDTO.getImageParameterDTO().getFileExtension());
            File fileToSaveImage = getFileToSave(fileName, true);

            if (imageDTO.getCompressedImage() != null && fileToSaveImage != null) {
                Path sourcePath = imageDTO.getCompressedImage().toPath();
                Path targetPath = fileToSaveImage.toPath();
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                if (!imageDTO.getCompressedImage().delete()) {
                    String temporaryFilePath = fileToSaveImage.getAbsolutePath();
                    log.warn("[{}] - Could not clean up temporary image: {}", ENTITY_NAME, temporaryFilePath);
                }

                log.debug("[{}] - Save new file to: {}", ENTITY_NAME, fileToSaveImage.getAbsolutePath());
                return BaseConfigConstants.UPLOAD_RESOURCES.PREFIX_PATH + fileName;
            }
        } catch (IOException e) {
            String filename = imageDTO.getImageParameterDTO().getOriginalImageFilename();
            log.error("[{}] - Could not save file: {}", ENTITY_NAME, filename, e);
        }

        return null;
    }

    public String save(MultipartFile file) {
        if (invalidUploadFile(file))
            return null;

        if (Objects.isNull(file.getOriginalFilename())) {
            log.warn("[{}] - The uploaded file has an invalid name or is empty", ENTITY_NAME);
            return null;
        }

        String fileName = generateUniqueFileName(file.getOriginalFilename());
        File directory = getFileToSave(fileName, true);

        if (Objects.isNull(directory))
            return null;

        try {
            file.transferTo(directory);
            return BaseConfigConstants.UPLOAD_RESOURCES.PREFIX_PATH + fileName;
        } catch (IOException e) {
            log.error("[{}] - Could not save this file to: {}", ENTITY_NAME, directory.getAbsolutePath(), e);
        }

        return null;
    }

    public List<String> save(MultipartFile[] files) {
        if (invalidUploadFiles(files))
            return Collections.emptyList();

        List<String> filePaths = new ArrayList<>();

        for (MultipartFile file : files) {
            String filePath = save(file);

            if (Objects.isNull(filePath))
                filePaths.add(BaseConfigConstants.UPLOAD_RESOURCES.DEFAULT_IMAGE_PATH_FOR_ERROR);
            else
                filePaths.add(filePath);
        }

        return filePaths;
    }

    public String autoCompressImageAndSave(MultipartFile image) {
        if (!BaseImageConverter.isValidImageFormat(image))
            return null;

        try {
            ImageDTO compressedImageFile = BaseImageConverter.compressImage(image);

            if (Objects.nonNull(compressedImageFile))
                return save(compressedImageFile);

            return save(image);
        } catch (IOException e) {
            log.error("[{}] - Could not auto compress image and save: {}", ENTITY_NAME, image.getOriginalFilename(), e);
        }

        return null;
    }

    public List<String> autoCompressImageAndSave(MultipartFile[] images) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            String imageUrl = autoCompressImageAndSave(image);

            if (StringUtils.hasText(imageUrl)) {
                imageUrls.add(imageUrl);
            }
        }

        return imageUrls;
    }

    public boolean delete(String filePath) {
        if (!StringUtils.hasText(filePath))
            return false;

        int positionPrefixPath = filePath.lastIndexOf(BaseConfigConstants.UPLOAD_RESOURCES.PREFIX_PATH);
        int prefixSize = BaseConfigConstants.UPLOAD_RESOURCES.PREFIX_PATH.length();
        String fileName = filePath.substring(positionPrefixPath + prefixSize);
        File file = getFileToSave(fileName, false);

        if (Objects.isNull(file))
            return false;

        log.debug("[{}] - Deleting file: {}", ENTITY_NAME, file.getAbsolutePath());
        return file.delete();
    }

    @Async
    public void delete(Collection<String> filePaths) {
        if (Objects.isNull(filePaths) || filePaths.isEmpty())
            return;

        for (String filePath : filePaths) {
            if (!delete(filePath)) {
                log.error("[{}] - Could not delete file: {}", ENTITY_NAME, filePath);
            }
        }
    }
}
