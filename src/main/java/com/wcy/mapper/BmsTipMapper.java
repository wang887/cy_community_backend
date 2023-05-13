package com.wcy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcy.model.entity.BmsTip;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public interface BmsTipMapper extends BaseMapper<BmsTip> {

    BmsTip getRandomTip();
}
