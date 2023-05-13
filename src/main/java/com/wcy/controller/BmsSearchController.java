package com.wcy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcy.common.api.ApiResult;
import com.wcy.model.vo.PostVO;
import com.wcy.service.IBmsTopicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/search")
public class BmsSearchController extends BaseController {

    @Resource
    private IBmsTopicService iBmsTopicService;

    @GetMapping
    public ApiResult<Page<PostVO>> searchList(@RequestParam("keyword") String keyword,
                                              @RequestParam("pageNum") Integer pageNum,
                                              @RequestParam("pageSize") Integer pageSize) {
        Page<PostVO> results = iBmsTopicService.searchByKey(keyword, new Page<>(pageNum, pageSize));
        return ApiResult.success(results);
    }

}
