package com.wcy.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wcy.model.dto.CreateQuestionDTO;
import com.wcy.model.entity.Question;
import com.wcy.model.entity.UmsUser;
import com.wcy.model.vo.QuestionVo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:09:25
 */
public interface QuestionService extends IService<Question> {


    /**
     * 分页获取问题
     * @param page
     * @param tab
     * @return
     */
    Page<QuestionVo> getList(Page<QuestionVo> page, String tab);

    /**
     * 创建一个问题
     * @param dto
     * @return
     */
    Question create(CreateQuestionDTO dto, UmsUser user);

    /**
     * 显示详情
     * @param userName  登录用户ming
     * @param id  question ID
     * @return
     */
    Map<String,Object> viewQuestion(String userName, String id);

    /**
     * 关注问题
     * @param id
     * @param mode  true为+1，false为-1
     */
    boolean follower(String id, String mode,String username);
}
