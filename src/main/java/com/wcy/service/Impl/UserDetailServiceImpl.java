package com.wcy.service.Impl;

import com.wcy.model.entity.UmsUser;
import com.wcy.model.vo.LoginUser;
import com.wcy.service.IBmsUmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Qualifier("IBmsUmsUserService")
    @Autowired
    private IBmsUmsUserService iBmsUmsUserService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UmsUser user = iBmsUmsUserService.getUserByUsername(username);

        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }

        // TODO 根据用户查询权限信息 添加到loginUser中
        //分装成UserDetails对象返回
        return new LoginUser(user);
    }
}
