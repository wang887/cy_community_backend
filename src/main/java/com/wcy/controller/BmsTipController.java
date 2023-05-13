package com.wcy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wcy.common.api.ApiResult;
import com.wcy.model.entity.BmsBillboard;
import com.wcy.model.entity.BmsTip;
import com.wcy.service.IBmsBillboardService;
import com.wcy.service.IBmsTipService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController  //可以自动的转成JSON文件
@RequestMapping("/tip")
public class BmsTipController extends BaseController {

    @Resource
    private IBmsTipService iBmsTipService;

    @GetMapping("/today")
    public ApiResult<BmsTip> getNotices(){
        BmsTip randomTip = iBmsTipService.getRandomTip();
        return ApiResult.success(randomTip);
    }
}
