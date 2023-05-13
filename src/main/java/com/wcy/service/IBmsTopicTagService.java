package com.wcy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wcy.model.entity.BmsTag;
import com.wcy.model.entity.BmsTopicTag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface IBmsTopicTagService extends IService<BmsTopicTag> {

    /**
     * 获取Topic Tag 关联记录
     * @param topicId
     * @return
     */
    List<BmsTopicTag> selectByTopicId(String topicId);

    /**
     * 创建中间关系
     * @param id
     * @param tags
     */
    void createTopicTag(String id, List<BmsTag> tags);

    /**
     *
     * @param id
     * @return
     */
    Set<String> selectTopicIdsByTagId(String id);
}
