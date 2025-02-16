package com.dct.base.web.rest;

import com.dct.base.common.FileUtils;
import com.dct.base.dto.response.BaseResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/common/upload")
public class ImageUploadResource {

    private static final Logger log = LoggerFactory.getLogger(ImageUploadResource.class);
    private final FileUtils fileUtils;

    public ImageUploadResource(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @PostMapping("/images")
    public BaseResponseDTO saveImages(@RequestParam("images") MultipartFile[] images) {
        log.debug("REST request to save files");
        List<String> filePaths = new ArrayList<>();

        for (MultipartFile image : images) {
            String imageFilePath = fileUtils.autoCompressImageAndSave(image);
            filePaths.add(imageFilePath);
        }

        return new BaseResponseDTO(filePaths);
    }
}
