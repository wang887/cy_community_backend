package com.wcy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wcy.model.entity.QuestionTag;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:11:46
 */
public interface QuestionTagService extends IService<QuestionTag> {

    /**
     * 保存标签
     * @param tags
     */
    List<QuestionTag> insertTag(List<String> tags);
}
