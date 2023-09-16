package com.wcy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wcy.common.api.ApiResult;
import com.wcy.model.dto.LoginDTO;
import com.wcy.model.dto.RegisterDTO;
import com.wcy.model.entity.BmsBillboard;
import com.wcy.model.entity.UmsUser;
import com.wcy.model.vo.ProfileVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IBmsUmsUserService extends IService<UmsUser> {

    /**
     * 注册功能
     * @param dto
     * @return 注册对象
     */
    UmsUser executeRegister(RegisterDTO dto) throws Exception;


    /**
     * 重置密码
     * @param dto
     * @return
     */
    Boolean resetPwd(RegisterDTO dto);
    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    UmsUser getUserByUsername(String username);

    /**
     * 通过用户查找集合
     * @param username
     * @return
     */
    List<UmsUser> getUserListByUsername(String username);


    /**
     * 登录功能
     * @param dto
     * @return 生成的JWT
     */
    String[] execuLogin(LoginDTO dto) throws Exception;

    ApiResult<Map<String,Object>> login(LoginDTO dto);


    /**
     * 激活用户
     * @param code 验证码
     */
    Boolean activeUser(String code);


    /**
     * 确认更改密码
     * @param code
     */
    Boolean submitRestPwd(String code);

    ProfileVO getUserProfile(String userId);
}
