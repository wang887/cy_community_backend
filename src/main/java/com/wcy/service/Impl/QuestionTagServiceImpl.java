package com.wcy.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wcy.common.exception.ApiAsserts;
import com.wcy.mapper.QuestionTagMapper;
import com.wcy.model.entity.QuestionTag;
import com.wcy.service.QuestionTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:11:46
 */
@Service
public class QuestionTagServiceImpl extends ServiceImpl<QuestionTagMapper, QuestionTag> implements QuestionTagService {

    @Override
    public List<QuestionTag> insertTag(List<String> tags) {
        List<QuestionTag> list = new ArrayList<>();
        for(String tag:tags){
            QuestionTag questionTag = baseMapper.selectOne(new LambdaQueryWrapper<QuestionTag>().eq(QuestionTag::getName, tag));
            if(ObjectUtils.isEmpty(questionTag)){
                questionTag = QuestionTag.builder()
                        .questionCount(0)
                        .name(tag)
                        .build();
                baseMapper.insert(questionTag);
            }else{
                questionTag.setQuestionCount(questionTag.getQuestionCount()+1);
                baseMapper.updateById(questionTag);
            }
            list.add(questionTag);
        }
        return list;
    }
}
