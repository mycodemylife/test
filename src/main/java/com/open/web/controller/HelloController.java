package com.open.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v2/hello")
public class HelloController {

    @RequestMapping("/test")
    private String test(){
        return "hello";
    }
}
