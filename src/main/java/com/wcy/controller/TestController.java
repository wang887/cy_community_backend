package com.wcy.controller;

import com.wcy.annotation.AccessLimit;
import com.wcy.common.api.ApiResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/hi")
    @AccessLimit(second = 13,maxTime = 5,forbiddenTime = 50)
    public ApiResult getData(){
        return ApiResult.success();
    }

}
