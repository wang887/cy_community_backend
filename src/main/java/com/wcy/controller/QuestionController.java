package com.wcy.controller;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcy.common.api.ApiResult;
import com.wcy.model.dto.CreateQuestionDTO;
import com.wcy.model.entity.Question;
import com.wcy.model.entity.UmsUser;
import com.wcy.model.vo.QuestionVo;
import com.wcy.service.IBmsUmsUserService;
import com.wcy.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

import static com.wcy.jwt.JwtUtil.USER_NAME;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:09:25
 */
@RestController
@RequestMapping("/question")
public class QuestionController extends BaseController {

    // TODO 加油！
    @Autowired
    private QuestionService questionService;

    @Autowired
    private IBmsUmsUserService iBmsUmsUserService;

    @GetMapping("/list")
    public ApiResult<Page<QuestionVo>> list(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                          @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                          @RequestParam(value = "size", defaultValue = "10") Integer pageSize){
        /**
         * username,content,createTime,support,view,answers,colletions
         */
        Page<QuestionVo> list = questionService.getList(new Page<>(pageNo, pageSize), tab);
        System.out.println(list.getRecords());
        return ApiResult.success(list);
    }

    @PostMapping("/create")
    public ApiResult<Question> create(@RequestBody CreateQuestionDTO dto, @RequestHeader(value = USER_NAME) String username){
        UmsUser user = iBmsUmsUserService.getUserByUsername(username);
        Question question = questionService.create(dto, user);
        return ApiResult.success(question);
    }

    @GetMapping("/del/{id}")
    public ApiResult<Question> del(@RequestHeader(value = USER_NAME) String userName, @PathVariable("id") String id){
        UmsUser umsUser = iBmsUmsUserService.getUserByUsername(userName);
        Question question = questionService.getById(id);
        Assert.notNull(question, "来晚一步，话题已不存在");
        Assert.isTrue(question.getUserId().equals(umsUser.getId()), "你为什么可以删除别人的问题？？？");
        questionService.removeById(id);

        // TODO 删除该问题下的回答

        return ApiResult.success(null, "删除成功");
    }

    @PostMapping("/detail")
    public ApiResult<Map<String,Object>> detail( @RequestParam("id") String id,@RequestParam("userId") String userId){
        System.out.println("userId:"+userId);
        Map<String,Object> question = questionService.viewQuestion(userId, id);
        return ApiResult.success(question);
    }

    /**
     *
     * @param id
     * @param mode  true为+1，false为-1
     * @return
     */
    @PostMapping("/support")
    public ApiResult<Map<String,Object>> support(@RequestParam("id") String id,@RequestParam("mode") String mode){
        Question question = questionService.getById(id);
        if(Objects.isNull(question)){
            return ApiResult.failed();
        }
        if(mode.equals("true")){
            question.setSupport(question.getSupport()+1);
        }else{
            question.setSupport(question.getSupport()-1);
        }
        questionService.updateById(question);
        return ApiResult.success(null);
    }

    /**
     *
     * @param id
     * @param mode  true为+1，false为-1
     * @return
     */
    @PostMapping("/follower")
    public ApiResult<Map<String,Object>> follower(@RequestParam("id") String id,@RequestParam("mode") String mode,@RequestHeader(value = USER_NAME) String username){
        boolean b = questionService.follower(id, mode, username);
        if(b){
            return ApiResult.success(null);
        }else{
            return ApiResult.failed();
        }
    }

}
