package com.wcy.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wcy.model.entity.QuestionRelationTag;
import com.wcy.model.entity.QuestionTag;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:12:09
 */
public interface QuestionRelationTagService extends IService<QuestionRelationTag> {

    /**
     * 创建问题与Tag的联系
     * @param questionTags
     */
    void createRelation(String questionId, List<QuestionTag> questionTags);
}
