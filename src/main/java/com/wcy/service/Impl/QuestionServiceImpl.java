package com.wcy.service.Impl;

import com.wcy.mapper.QuestionMapper;
import com.wcy.model.entity.Question;
import com.wcy.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
