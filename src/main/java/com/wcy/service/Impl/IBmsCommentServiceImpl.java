package com.wcy.service.Impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcy.mapper.BmsCommentMapper;
import com.wcy.model.dto.CommentDTO;
import com.wcy.model.entity.BmsComment;
import com.wcy.model.entity.BmsCommentFollow;
import com.wcy.model.entity.UmsUser;
import com.wcy.model.vo.CommentVO;
import com.wcy.service.IBmsCommentFollowService;
import com.wcy.service.IBmsCommentService;
import com.wcy.service.IBmsUmsUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class IBmsCommentServiceImpl extends ServiceImpl<BmsCommentMapper, BmsComment> implements IBmsCommentService {
    @Autowired
    private IBmsCommentFollowService iBmsCommentFollowService;

    @Autowired
    private IBmsUmsUserService iBmsUmsUserService;
    @Override
    public List<CommentVO> getCommentsByTopicID(String topicid) {
        List<CommentVO> lstBmsComment = new ArrayList<CommentVO>();
        try {
            lstBmsComment = this.baseMapper.getCommentsByTopicID(topicid);
            //对每一个评论的子评论进行获取
            for(CommentVO vo:lstBmsComment){
                List<BmsCommentFollow> list = iBmsCommentFollowService.list(new LambdaQueryWrapper<BmsCommentFollow>().eq(BmsCommentFollow::getParentId, vo.getId()));
                vo.setReplayNum(list.size());
                for(BmsCommentFollow bms:list) {
                    CommentVO commentChild = baseMapper.getCommentsById(bms.getFollowerId());
//                    commentChild.setToUserId(vo.getToUserId());
                    //设置回复谁的
                    if(commentChild.getToUserId()!=null || !commentChild.getToUserId().equals("")){
                        UmsUser toUser = iBmsUmsUserService.getById(commentChild.getToUserId());
                        commentChild.setToUsername(toUser.getUsername());
                    }else{
                        commentChild.setToUsername(vo.getUsername());
                    }

                    List<CommentVO> child = vo.getChild();
                    child.add(commentChild);
                    vo.setChild(child);
                }
            }
        } catch (Exception e) {
            log.info("lstBmsComment失败");
        }
        return lstBmsComment;
    }

    @Override
    public BmsComment create(CommentDTO dto, UmsUser user) {
        System.out.println(dto);
        BmsComment comment;
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        String code = snowflake.nextIdStr();
        if(dto.getParents_id()!=null){                //子评论
            comment = BmsComment.builder()
                    .id(code)
                    .userId(user.getId())
                    .toUserId(dto.getUser_id())
                    .content(dto.getContent())
                    .topicId(dto.getTopic_id())
                    .createTime(new Date())
                    .level(0)
                    .support(0)
                    .build();
            //写入评论父节点与子节点关系
            BmsCommentFollow commentFollow = BmsCommentFollow.builder()
                    .parentId(dto.getParents_id())
                    .followerId(code)
                    .build();
            iBmsCommentFollowService.save(commentFollow);
        }else{
            comment = BmsComment.builder()
                    .id(code)
                    .userId(user.getId())
                    .content(dto.getContent())
                    .topicId(dto.getTopic_id())
                    .createTime(new Date())
                    .level(1)
                    .support(0)
                    .build();
        }
        this.baseMapper.insert(comment);
        return comment;
    }

    @Override
    public Boolean agree(String id) {
        BmsComment comment = baseMapper.selectById(id);
        if(ObjectUtils.isEmpty(comment)){
            return false;
        }
        comment.setSupport(comment.getSupport()+1);
        int i = baseMapper.updateById(comment);
        if(i==0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public Boolean unAgree(String id) {
        BmsComment comment = baseMapper.selectById(id);
        if(ObjectUtils.isEmpty(comment)){
            return false;
        }
        comment.setSupport(comment.getSupport()-1);
        int i = baseMapper.updateById(comment);
        if(i==0){
            return false;
        }else{
            return true;
        }
    }
}
