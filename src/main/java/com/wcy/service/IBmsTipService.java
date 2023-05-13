package com.wcy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wcy.model.entity.BmsTip;
import org.springframework.stereotype.Service;

@Service
public interface IBmsTipService extends IService<BmsTip> {

    /**
     * 随机产生一条每日一句
     * @return
     */
    BmsTip getRandomTip();
}
