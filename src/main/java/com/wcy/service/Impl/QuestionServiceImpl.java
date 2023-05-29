package com.wcy.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcy.common.api.ApiResult;
import com.wcy.mapper.QuestionMapper;
import com.wcy.model.dto.CreateQuestionDTO;
import com.wcy.model.entity.*;
import com.wcy.model.vo.QuestionVo;
import com.wcy.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:09:25
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private QuestionRelationTagService questionRelationTagService;

    @Autowired
    private QuestionTagService questionTagService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private IBmsUmsUserService iBmsUmsUserService;

    @Autowired
    private UserQuestionFollowerService userQuestionFollowerService;

    @Override
    public Page<QuestionVo> getList(Page<QuestionVo> page, String tab) {
        Page<QuestionVo> iPage = baseMapper.selectListAndPage(page, tab);
        // 查询问题的标签
        iPage.getRecords().forEach(question -> {
            List<QuestionRelationTag> list = questionRelationTagService.list(new LambdaQueryWrapper<QuestionRelationTag>().eq(QuestionRelationTag::getQuestionId, question.getId()));
            if (!list.isEmpty()) {
                List<String> tagIds = list.stream().map(QuestionRelationTag::getTagId).collect(Collectors.toList());
                List<QuestionTag> tags = questionTagService.listByIds(tagIds);
                question.setTags(tags);
            }
        });
        return iPage;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Question create(CreateQuestionDTO dto, UmsUser user) {

        Question question = Question.builder()
                .content(dto.getQuestion())
                .userId(user.getId())
                .followers(0)
                .collections(0)
                .answers(0)
                .createTime(new Date())
                .view(0)
                .build();
        baseMapper.insert(question);

        //用户积分
        int newScore = user.getScore() + 1;
        iBmsUmsUserService.updateById(user.setScore(newScore));

        // 标签
        if (!ObjectUtils.isEmpty(dto.getTags())) {
            // TODO 保存标签
            List<QuestionTag> questionTags = questionTagService.insertTag(dto.getTags());
            // TODO 处理标签与问题的关联
            questionRelationTagService.createRelation(question.getId(),questionTags);
        }
        return question;
    }

    @Override
    public Map<String, Object> viewQuestion(String userName,String id) {
        Map<String,Object> map = new HashMap<>(16);
        //找出问题
        Question question = baseMapper.selectById(id);
        map.put("question",question);
        //找出标签
        List<QuestionRelationTag> list = questionRelationTagService.list(new LambdaQueryWrapper<QuestionRelationTag>().eq(QuestionRelationTag::getQuestionId, id));
        List<QuestionTag> tags = new ArrayList<>();
        for(QuestionRelationTag rtag : list){
            QuestionTag tag = questionTagService.getById(rtag.getTagId());
            tags.add(tag);
        }
        map.put("tags",tags);
        //找出回答
        List<Answer> answerList = answerService.list(new LambdaQueryWrapper<Answer>().eq(Answer::getQuestionId, id).orderByDesc(Answer::getCreateTime));
        map.put("answerList",answerList);
        //用户
        UmsUser user = iBmsUmsUserService.getById(question.getUserId());
        map.put("user",user);

        //当前用户是否关注此问题
        UmsUser currentUser = iBmsUmsUserService.getById(userName);
        if(Objects.isNull(currentUser)){
            map.put("isFollower",false);
        }else{
            UserQuestionFollower one = userQuestionFollowerService.getOne(new LambdaQueryWrapper<UserQuestionFollower>().eq(UserQuestionFollower::getQuestionId, id)
                    .eq(UserQuestionFollower::getUserId, currentUser.getId()));
            if(Objects.isNull(one)){
                map.put("isFollower",false);
            }else{
                map.put("isFollower",true);
            }
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean follower(String id, String mode,String username) {
        Question question = baseMapper.selectById(id);
        if(Objects.isNull(question)){
            return false;
        }
        UmsUser user = iBmsUmsUserService.getUserByUsername(username);
        if(Objects.isNull(user)){
            return false;
        }
        if(mode.equals("true")){
            question.setFollowers(question.getFollowers()+1);
            //增加userID 与 questionId在userQuestionTable的映射
            UserQuestionFollower f = new UserQuestionFollower();
            f.setQuestionId(id);
            f.setUserId(user.getId());
            userQuestionFollowerService.save(f);
        }else{
            question.setFollowers(question.getFollowers()-1);
            UserQuestionFollower one = userQuestionFollowerService.getOne(new LambdaQueryWrapper<UserQuestionFollower>().eq(UserQuestionFollower::getQuestionId, id)
                    .eq(UserQuestionFollower::getUserId, user.getId()));
            if(Objects.isNull(one)){
                return false;
            }
            userQuestionFollowerService.removeById(one.getId());
        }
        baseMapper.updateById(question);
        return true;
    }
}
