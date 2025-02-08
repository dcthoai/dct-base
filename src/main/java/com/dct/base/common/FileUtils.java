package com.dct.base.common;

import com.dct.base.config.properties.UploadResourceProperties;
import com.dct.base.constants.BaseConstants;
import com.dct.base.dto.ImageDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final String UPLOAD_PATH;

    public FileUtils(@Qualifier("uploadResourceProperties") UploadResourceProperties uploadResourceProperties) {
        UPLOAD_PATH = uploadResourceProperties.getUploadPath();
    }

    public static boolean isInvalidUploadFile(MultipartFile file) {
        return file == null || file.isEmpty() || !Objects.nonNull(file.getOriginalFilename());
    }

    public static boolean isInvalidUploadFile(MultipartFile[] files) {
        return files != null && files.length > 0;
    }

    private File getFileToSave(String fileName, boolean isMakeNew) {
        File file = new File(UPLOAD_PATH + File.separator + fileName);

        if (file.exists() || !isMakeNew)
            return file;

        try {
            // Make sure the parent directory exists
            File parentDir = file.getParentFile();

            if (Objects.nonNull(parentDir) && !parentDir.exists() && !parentDir.mkdirs()) {
                log.warn("Could not create parent directory: {}", parentDir.getAbsolutePath());
                return null;
            }

            return file.createNewFile() ? file : null;
        } catch (Exception e) {
            log.warn("Could not create new file at: {}", file.getAbsolutePath());
        }

        return null;
    }

    public static String generateUniqueFileName(String fileNameOrFileExtension) {
        String uniqueName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));

        if (Objects.isNull(fileNameOrFileExtension))
            return uniqueName + ".webp";

        if (fileNameOrFileExtension.startsWith("."))
            return uniqueName + fileNameOrFileExtension;

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

                if (!imageDTO.getCompressedImage().delete())
                    log.warn("Could not clean up temporary image file: {}", fileToSaveImage.getAbsolutePath());

                return BaseConstants.UPLOAD_RESOURCES.PREFIX_PATH + fileName;
            }
        } catch (IOException e) {
            log.error("Could not save file: {}", imageDTO.getImageParameterDTO().getOriginalImageFilename(), e);
        }

        return null;
    }

    public String save(MultipartFile file) {
        if (isInvalidUploadFile(file))
            return null;

        if (Objects.isNull(file.getOriginalFilename())) {
            log.warn("The uploaded file has an invalid name or is empty");
            return null;
        }

        String fileName = generateUniqueFileName(file.getOriginalFilename());
        File directory = getFileToSave(fileName, true);

        if (Objects.isNull(directory))
            return null;

        try {
            file.transferTo(directory);
            return BaseConstants.UPLOAD_RESOURCES.PREFIX_PATH + fileName;
        } catch (IOException e) {
            log.error("Could not save this file to: {}", directory.getAbsolutePath(), e);
        }

        return null;
    }

    public List<String> save(MultipartFile[] files) {
        if (isInvalidUploadFile(files))
            return Collections.emptyList();

        List<String> filePaths = new ArrayList<>();

        for (MultipartFile file : files) {
            String filePath = save(file);

            if (Objects.isNull(filePath))
                filePaths.add("");
            else
                filePaths.add(filePath);
        }

        return filePaths;
    }

    public String autoCompressImageAndSave(MultipartFile image) throws IOException {
        if (!ImageConverter.isValidImageFormat(image))
            return null;

        return save(ImageConverter.compressImage(image));
    }

    public boolean delete(String filePath) {
        if (!StringUtils.hasText(filePath))
            return false;

        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        File file = getFileToSave(fileName, false);

        if (Objects.isNull(file))
            return false;

        return file.delete();
    }

    public boolean delete(List<String> filePaths) {
        if (Objects.isNull(filePaths) || filePaths.isEmpty())
            return false;

        for (String filePath : filePaths)
            if (!delete(filePath))
                log.error("Could not delete file: {}", filePath);

        return true;
    }
}
