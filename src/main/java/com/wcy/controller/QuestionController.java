package com.wcy.controller;

import com.wcy.common.api.ApiResult;
import com.wcy.model.entity.Question;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:09:25
 */
@Controller
@RequestMapping("/question")
public class QuestionController extends BaseController {

    // TODO 加油！

    @GetMapping("/list")
    public ApiResult<List<Question>> list(){
        return null;
    }

    @PostMapping("/create")
    public ApiResult<Question> create(){
        return null;
    }

    @GetMapping("/del")
    public ApiResult<Question> del(){
        return null;
    }

    @GetMapping("/detail")
    public ApiResult<Question> detail(){
        return null;
    }

}
