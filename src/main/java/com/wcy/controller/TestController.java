package com.wcy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class TestController {
    @RequestMapping("/hi")
    @ResponseBody
    public String getData(){
        return "hello";
    }

}
