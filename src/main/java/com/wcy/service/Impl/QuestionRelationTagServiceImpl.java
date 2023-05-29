package com.wcy.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wcy.mapper.QuestionRelationTagMapper;
import com.wcy.model.entity.QuestionRelationTag;
import com.wcy.model.entity.QuestionTag;
import com.wcy.service.QuestionRelationTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:12:09
 */
@Service
public class QuestionRelationTagServiceImpl extends ServiceImpl<QuestionRelationTagMapper, QuestionRelationTag> implements QuestionRelationTagService {

    @Override
    public void createRelation(String questionId, List<QuestionTag> questionTags) {
        baseMapper.delete(new LambdaQueryWrapper<QuestionRelationTag>().eq(QuestionRelationTag::getQuestionId,questionId));

        questionTags.forEach(tag->{
            QuestionRelationTag questionRelationTag = QuestionRelationTag.builder()
                    .questionId(questionId)
                    .tagId(tag.getId())
                    .build();
            baseMapper.insert(questionRelationTag);
        });

    }
}
