package com.dct.base.web;

import com.dct.base.dto.response.BaseResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Test {

    @PostMapping("/users/auth")
    public BaseResponseDTO test() {
        return new BaseResponseDTO();
    }
}
