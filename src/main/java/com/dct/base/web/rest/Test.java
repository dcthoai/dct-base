package com.dct.base.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("/users/test")
    public String test() {
        System.out.println("Test au");
        return "Authenticated";
    }
}
