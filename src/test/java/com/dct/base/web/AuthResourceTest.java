package com.dct.base.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AuthResourceTest {

    @GetMapping
    @ResponseBody
    public String getAuthorizationCode() {
        return "Authenticated successful";
    }
}
