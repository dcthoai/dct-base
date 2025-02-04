package com.dct.base.web.rest;

import com.dct.base.common.FileUtils;
import com.dct.base.dto.response.BaseResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/p")
public class ImageUploadResource {

    private final FileUtils fileUtils;

    public ImageUploadResource(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @PostMapping("/image")
    public BaseResponseDTO saveImage(@RequestParam("image") MultipartFile image) {
        try {
            String filePath = fileUtils.autoCompressImageAndSave(image);
            return new BaseResponseDTO(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new BaseResponseDTO();
    }
}
