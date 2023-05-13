package com.wcy.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcy.mapper.BmsBillboardMapper;
import com.wcy.mapper.BmsPromotionMapper;
import com.wcy.model.entity.BmsBillboard;
import com.wcy.model.entity.BmsPromotion;
import com.wcy.service.IBmsBillboardService;
import com.wcy.service.IBmsPromotionService;
import org.springframework.stereotype.Service;

@Service
public class IBmsPromotionServiceImpl extends ServiceImpl<BmsPromotionMapper, BmsPromotion>
        implements IBmsPromotionService {

}
