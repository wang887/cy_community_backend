package com.wcy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vdurmont.emoji.EmojiParser;
import com.wcy.common.api.ApiResult;
import com.wcy.model.dto.CommentDTO;
import com.wcy.model.dto.CreateTopicDTO;
import com.wcy.model.dto.UpdateTopicDTO;
import com.wcy.model.entity.*;
import com.wcy.model.vo.CommentVO;
import com.wcy.model.vo.PostVO;
import com.wcy.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.wcy.jwt.JwtUtil.USER_NAME;

@RestController
@RequestMapping("/post")
public class BmsTopicController extends BaseController {


    @Resource
    private IBmsTopicService iBmsTopicService;

    @Resource
    private IBmsTagService iBmsTagService;

    @Resource
    private IBmsTopicTagService iBmsTopicTagService;

    @Resource
    private IBmsUmsUserService iBmsUmsUserService;

    @Resource
    private IBmsCommentService iBmsCommentService;

    @GetMapping("/list")
    public ApiResult<Page<PostVO>> list(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<PostVO> list = iBmsTopicService.getList(new Page<>(pageNo, pageSize), tab);
        return ApiResult.success(list);
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ApiResult<BmsTopic> create(@RequestHeader(value = USER_NAME) String username, @RequestBody CreateTopicDTO dto){
        UmsUser user = iBmsUmsUserService.getUserByUsername(username);
        BmsTopic topic = iBmsTopicService.create(dto,user);
        return ApiResult.success(topic,"创建成功");
    }


    @GetMapping()
    public ApiResult<Map<String,Object>> view(@RequestParam("id") String id){
        Map<String,Object> map = iBmsTopicService.viewTopic(id);
        return ApiResult.success(map);
    }

    @GetMapping("/recommend")
    public ApiResult<List<BmsTopic>> getRecommend(@RequestParam("topicId") String id) {
        List<BmsTopic> topics = iBmsTopicService.getRecommend(id);
        return ApiResult.success(topics);
    }

    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<UpdateTopicDTO> update(@RequestHeader(value = USER_NAME) String userName,
                                            @Valid @RequestBody(required = true) UpdateTopicDTO params){
//                                      @Valid @RequestBody BmsTopic topic) {
        UmsUser umsUser = iBmsUmsUserService.getUserByUsername(userName);
        System.out.println(params);
        BmsTopic topic = params.getTopic();
        Assert.isTrue(umsUser.getId().equals(topic.getUserId()), "非本人无权修改");
        UpdateTopicDTO update = iBmsTopicService.update(params);
        return ApiResult.success(update);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResult<String> delete(@RequestHeader(value = USER_NAME) String userName, @PathVariable("id") String id) {
        UmsUser umsUser = iBmsUmsUserService.getUserByUsername(userName);
        BmsTopic byId = iBmsTopicService.getById(id);
        Assert.notNull(byId, "来晚一步，话题已不存在");
        Assert.isTrue(byId.getUserId().equals(umsUser.getId()), "你为什么可以删除别人的话题？？？");
        iBmsTopicService.removeById(id);

        //删除该帖子下面的评论
        List<CommentVO> commentsByTopicID = iBmsCommentService.getCommentsByTopicID(id);
        for (CommentVO commentVO:commentsByTopicID){
            iBmsCommentService.remove(new LambdaQueryWrapper<BmsComment>().eq(BmsComment::getId,commentVO.getId()));
        }
        return ApiResult.success(null,"删除成功");
    }
}
