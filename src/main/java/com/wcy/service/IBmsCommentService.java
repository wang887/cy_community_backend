package com.wcy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.wcy.model.dto.CommentDTO;
import com.wcy.model.entity.BmsComment;
import com.wcy.model.entity.UmsUser;
import com.wcy.model.vo.CommentVO;


import java.util.List;


public interface IBmsCommentService extends IService<BmsComment> {
    /**
     *
     *
     * @param topicid
     * @return {@link BmsComment}
     */
    List<CommentVO> getCommentsByTopicID(String topicid);

    /**
     * 创造
     * @param dto
     * @param principal
     * @return
     */
    BmsComment create(CommentDTO dto, UmsUser principal);

    /**
     * 根据id，点赞量+1
     * @param id   评论id
     * @return
     */
    Boolean agree(String id);

    /**
     * 根据id,点赞量-1
     * @param id
     * @return
     */
    Boolean unAgree(String id);
}
