package com.wcy.controller;

import com.wcy.common.api.ApiResult;
import com.wcy.model.entity.Answer;
import com.wcy.model.entity.Question;
import com.wcy.service.AnswerService;
import com.wcy.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:10:31
 */
@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;
    @PostMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Map<String,Object>> create(@RequestBody Answer answer){
        System.out.println("answer="+answer);
        answer.setSupport(0);
        answer.setCreateTime(new Date());
        //存储回答
        answerService.save(answer);
        //回答数加1
        String questionId = answer.getQuestionId();
        Question question = questionService.getById(questionId);
        question.setAnswers(question.getAnswers()+1);
        questionService.updateById(question);
        return ApiResult.success(null,"success");
    }

    @PostMapping("/support")
    public ApiResult<Map<String,Object>> support(@RequestParam("id") String id, @RequestParam("mode") String mode){
        Answer answer = answerService.getById(id);
        if(Objects.isNull(answer)){
            return ApiResult.failed();
        }
        if(mode.equals("true")){
            answer.setSupport(answer.getSupport()+1);
        }else{
            answer.setSupport(answer.getSupport()-1);
        }
        answerService.updateById(answer);
        return ApiResult.success(null );
    }
}
