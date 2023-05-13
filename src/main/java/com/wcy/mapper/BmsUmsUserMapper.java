package com.wcy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wcy.model.entity.BmsBillboard;
import com.wcy.model.entity.UmsUser;
import org.springframework.stereotype.Repository;

@Repository     //可创建实体
/**
 * 继承mybatis-plus，自定义简单的数据库查询语句，BaseMapper<可操作实体类>
 */
public interface BmsUmsUserMapper extends BaseMapper<UmsUser> {


}
