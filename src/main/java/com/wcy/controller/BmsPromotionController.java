package com.wcy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wcy.common.api.ApiResult;
import com.wcy.model.entity.BmsBillboard;
import com.wcy.model.entity.BmsPromotion;
import com.wcy.service.IBmsBillboardService;
import com.wcy.service.IBmsPromotionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController  //可以自动的转成JSON文件
@RequestMapping("/promotion")
public class BmsPromotionController extends BaseController {

    @Resource
    private IBmsPromotionService iBmsPromotionService;

    @GetMapping("/show")
    public ApiResult<List<BmsPromotion>> getNotices(){
        List<BmsPromotion> list = iBmsPromotionService.list();
        return ApiResult.success(list);
    }
}
