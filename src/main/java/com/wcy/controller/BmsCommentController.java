package com.wcy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wcy.common.api.ApiResult;
import com.wcy.model.dto.CommentDTO;
import com.wcy.model.entity.BmsBillboard;
import com.wcy.model.entity.BmsComment;
import com.wcy.model.entity.BmsTopic;
import com.wcy.model.entity.UmsUser;
import com.wcy.model.vo.CommentVO;
import com.wcy.service.IBmsCommentService;
import com.wcy.service.IBmsTopicService;
import com.wcy.service.IBmsUmsUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wcy.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/comment")
public class BmsCommentController extends BaseController {

    @Resource
    private IBmsCommentService bmsCommentService;
    @Resource
    private IBmsUmsUserService umsUserService;


    @Resource
    private IBmsTopicService iBmsTopicService;

    @GetMapping("/get_comments")
    public ApiResult<List<CommentVO>> getCommentsByTopicID(@RequestParam(value = "topicid", defaultValue = "1") String topicid) {
        List<CommentVO> lstBmsComment = bmsCommentService.getCommentsByTopicID(topicid);
        return ApiResult.success(lstBmsComment);
    }
    @PostMapping("/add_comment")
    public ApiResult<BmsComment> add_comment(@RequestHeader(value = USER_NAME) String userName,
                                             @RequestBody CommentDTO dto) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        BmsComment comment = bmsCommentService.create(dto, user);
        //帖子的评论数量+1
        BmsTopic topic = iBmsTopicService.getById(dto.getTopic_id());
        topic.setComments(topic.getComments()+1);
        iBmsTopicService.updateById(topic);
        return ApiResult.success(comment);
    }

    @GetMapping("/agree")
    public ApiResult<String> agree(@RequestParam("commentId") String commentId){
        Boolean b = bmsCommentService.agree(commentId);
        return b?ApiResult.success("success"):ApiResult.failed("fail");
    }

    @GetMapping("/unagree")
    public ApiResult<String> unagree(@RequestParam("commentId") String commentId){
        Boolean b = bmsCommentService.unAgree(commentId);
        return b?ApiResult.success("success"):ApiResult.failed("fail");
    }
}
