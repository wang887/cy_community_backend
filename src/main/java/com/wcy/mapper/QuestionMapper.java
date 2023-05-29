package com.wcy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcy.model.entity.Question;
import com.wcy.model.vo.PostVO;
import com.wcy.model.vo.QuestionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wcy
 * @since 2023-05-13 05:09:25
 */
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 分页查询
     * @param page
     * @param tab
     * @return
     */
   Page<QuestionVo> selectListAndPage(@Param("page") Page<QuestionVo> page, @Param("tab") String tab);
}
