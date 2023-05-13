package com.wcy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcy.model.dto.CreateTopicDTO;
import com.wcy.model.dto.UpdateTopicDTO;
import com.wcy.model.entity.BmsTopic;
import com.wcy.model.entity.UmsUser;
import com.wcy.model.vo.PostVO;

import java.util.List;
import java.util.Map;

public interface IBmsTopicService extends IService<BmsTopic> {

    /**
     * 获取首页话题列表
     * @param page
     * @param tab
     * @return
     */
    Page<PostVO> getList(Page<PostVO> page, String tab);


    /**
     * 发布
     * @param dto
     * @param user
     * @return
     */
    BmsTopic create(CreateTopicDTO dto, UmsUser user);

    /**
     * 通过id显示详情
     * @param id
     * @return
     */
    Map<String, Object> viewTopic(String id);

    /**
     * 获取随机推荐10篇
     *
     * @param id
     * @return
     */
    List<BmsTopic> getRecommend(String id);

    /**
     * 关键字检索
     *
     * @param keyword
     * @param page
     * @return
     */
    Page<PostVO> searchByKey(String keyword, Page<PostVO> page);


    /**
     * 更新topic
     * @param dto
     * @return
     */
    UpdateTopicDTO update(UpdateTopicDTO dto);
}
