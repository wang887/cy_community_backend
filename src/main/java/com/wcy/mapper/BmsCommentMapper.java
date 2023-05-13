package com.wcy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcy.model.entity.BmsComment;
import com.wcy.model.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BmsCommentMapper extends BaseMapper<BmsComment> {

    /**
     * getCommentsByTopicID
     *
     * @param topicid
     * @return
     */
    List<CommentVO> getCommentsByTopicID(@Param("topicid") String topicid);

    /**
     * getCommentsById
     * @param followerId
     */
    CommentVO getCommentsById(String followerId);
}
