package com.dct.base.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class Welcome {

    private static final Logger log = LoggerFactory.getLogger(Welcome.class);

    @GetMapping(value = {"", "/"})
    @ResponseBody
    public String welcome() {
        log.info("Welcome to homepage!!!");
        return "<h1 style=\"width: 300px; margin: 0 auto; margin-top: 45vh;\">Welcome to DCT</h1>";
    }

    @GetMapping(value = "/test")
    @ResponseBody
    public String test() {
        return "Authenticated successful!!!";
    }
}
