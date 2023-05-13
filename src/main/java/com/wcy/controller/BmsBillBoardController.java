package com.wcy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wcy.common.api.ApiResult;
import com.wcy.model.entity.BmsBillboard;
import com.wcy.service.IBmsBillboardService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController  //可以自动的转成JSON文件
@RequestMapping("/billboard")
public class BmsBillBoardController extends BaseController {

    @Resource
    private IBmsBillboardService iBmsBillboardService;

    @GetMapping("/show")
    public ApiResult<BmsBillboard> getNotices(){
        List<BmsBillboard> list = iBmsBillboardService.list(new
                        LambdaQueryWrapper<BmsBillboard>().eq(BmsBillboard::isShow,1)
                );
        return ApiResult.success(list.get(list.size()-1));
    }
}
