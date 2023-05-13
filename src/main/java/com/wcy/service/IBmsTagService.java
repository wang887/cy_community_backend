package com.wcy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcy.model.entity.BmsTag;
import com.wcy.model.entity.BmsTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBmsTagService extends IService<BmsTag> {
    /**
     * 插入标签
     *
     * @param tags
     * @return
     */
    List<BmsTag> insertTags(List<String> tags);

    /**
     *
     * @param topicPage
     * @param id
     * @return
     */
    Page<BmsTopic> selectTopicsByTagId(Page<BmsTopic> topicPage, String id);
}