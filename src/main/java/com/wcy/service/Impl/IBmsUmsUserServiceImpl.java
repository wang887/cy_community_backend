package com.wcy.service.Impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wcy.common.api.ApiResult;
import com.wcy.common.exception.ApiAsserts;
import com.wcy.jwt.JwtUtil;
import com.wcy.mapper.BmsFollowMapper;
import com.wcy.mapper.BmsTopicMapper;
import com.wcy.mapper.BmsUmsUserMapper;
import com.wcy.model.dto.LoginDTO;
import com.wcy.model.dto.RegisterDTO;
import com.wcy.model.entity.BmsFollow;
import com.wcy.model.entity.BmsTopic;
import com.wcy.model.entity.UmsUser;
import com.wcy.model.vo.LoginUser;
import com.wcy.model.vo.ProfileVO;
import com.wcy.service.EmailService;
import com.wcy.service.IBmsUmsUserService;
import com.wcy.service.RedisService;
import com.wcy.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class IBmsUmsUserServiceImpl extends ServiceImpl<BmsUmsUserMapper, UmsUser>
        implements IBmsUmsUserService{

    @Autowired
    private BmsTopicMapper bmsTopicMapper;

    @Autowired
    private BmsFollowMapper bmsFollowMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AuthenticationManager authenticationManager;




    @Override
    public UmsUser executeRegister(RegisterDTO dto) throws Exception {
        //查询是否有相同的用户名
        LambdaQueryWrapper<UmsUser> umsUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        umsUserLambdaQueryWrapper.eq(UmsUser::getUsername,dto.getName()).or().eq(UmsUser::getEmail,dto.getEmail());
        UmsUser umsUser = baseMapper.selectOne(umsUserLambdaQueryWrapper);
        if(!ObjectUtils.isEmpty(umsUser)){
            ApiAsserts.fail("账号或邮箱已存在");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UmsUser addUser = UmsUser.builder()     //链式创建对象 @Build注解  set方法一致
                .username(dto.getName())
                .alias(dto.getName())
//                .password(MD5Utils.getPwd(dto.getPass()))  //对密码进行MD5加密
                .password(bCryptPasswordEncoder.encode(dto.getPass()))
                .email(dto.getEmail())
                .createTime(new Date())
                .status(true)
                .active(false)
                .build();
        try {
            baseMapper.insert(addUser);
            //注册成功需要激活
           // 发送激活连接  生成激活码
            Snowflake snowflake = IdUtil.createSnowflake(1, 1);
            String code = snowflake.nextIdStr();
            //激活码为key,addUser为value存入redis,并设置有效期10min
            redisService.set(code, JSON.toJSONString(addUser),600);
//            emailService.sendCode(addUser.getEmail(),"欢迎注册","点击下面连接进行激活，有效期10分钟：http://8.130.113.22:8001/#/ums/user/active?code="+code);
            emailService.sendCode(addUser.getEmail(),"欢迎注册","点击下面连接进行激活，有效期10分钟：http://8.130.113.22:8000/ums/user/active?code="+code);
        }catch (Exception e){
            throw new Exception("注册失败");
        }
        return addUser;
    }

    @Override
    public Boolean resetPwd(RegisterDTO dto) {
        UmsUser user = baseMapper.selectOne(new LambdaQueryWrapper<UmsUser>().eq(UmsUser::getEmail, dto.getEmail()));
        if (ObjectUtils.isEmpty(user)){
            return false;
        }
        //发送邮箱
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        String code = snowflake.nextIdStr();
        //激活码为key,addUser为value存入redis,并设置有效期10min
        redisService.set(code, JSON.toJSONString(dto),600);
        emailService.sendCode(dto.getEmail(),"重置密码","点击下面连接进行确认重置，有效期10分钟：http://8.130.113.22:8000/ums/user/submitResetPwd?code="+code);
        return true;
    }

    @Override
    public UmsUser getUserByUsername(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<UmsUser>().eq(UmsUser::getUsername,username).eq(UmsUser::getActive,1));
    }

    @Override
    public List<UmsUser> getUserListByUsername(String username) {
        return baseMapper.selectList(new LambdaQueryWrapper<UmsUser>().eq(UmsUser::getUsername,username).eq(UmsUser::getActive,1));
    }

    @Override
    public String[] execuLogin(LoginDTO dto) throws Exception {
        String token = null;
        String msg=null;
        String[] res = {token, msg};
        try {
            List<UmsUser> users = getUserListByUsername(dto.getUsername());
            if (users==null || users.isEmpty()){
                res[1]="账号未激活";
                return res;
            }
            if(users.size()>1){
                res[1]="账号异常，请联系管理员";
                return res;
            }
            UmsUser user = users.get(0);
            String encodePwd = MD5Utils.getPwd(dto.getPassword());
            if(!encodePwd.equals(user.getPassword())){
                res[1]="密码错误";
                throw new Exception("密码错误");
            }
            token = JwtUtil.generateToken(user.getUsername());
            res[0] =token;
        }catch (Exception e){
            log.warn("用户名不存在或密码验证失败---{}",dto.getUsername());
        }
        return res;

    }

    @Override
    public ApiResult<Map<String, Object>> login(LoginDTO dto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if(Objects.isNull(authentication)){
            return ApiResult.failed("用户名或者密码错误");
        }
        //使用Username生成token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userName = loginUser.getUser().getUsername().toString();

        String jwt = JwtUtil.generateToken(userName);
        //将authenticate存入redis
        redisService.set("login:"+userName,JSON.toJSONString(loginUser));
        //将token相应给前端
        Map<String,Object> map = new HashMap<>();
        map.put("token",jwt);
        return ApiResult.success(map,"登录成功");
    }

    @Override
    public Boolean activeUser(String code) {
        if(redisService.exists(code)==false){
            ApiAsserts.fail("验证码过期");
            return false;
        }else{
            String json = (String) redisService.get(code);
            UmsUser user = JSON.parseObject(json, UmsUser.class);
            user.setActive(true);
            baseMapper.updateById(user);
            return true;
        }
    }

    @Override
    public Boolean submitRestPwd(String code) {
        if(!redisService.exists(code)){
            ApiAsserts.fail("验证码过期");
            return false;
        }else{
            String json = (String) redisService.get(code);
            RegisterDTO dto = JSON.parseObject(json, RegisterDTO.class);
            UmsUser user = baseMapper.selectOne(new LambdaQueryWrapper<UmsUser>().eq(UmsUser::getEmail, dto.getEmail()));
            user.setPassword(MD5Utils.getPwd(dto.getPass()));
            baseMapper.updateById(user);
            return true;
        }
    }

    @Override
    public ProfileVO getUserProfile(String userId) {
        ProfileVO profileVO = new ProfileVO();
        UmsUser user = baseMapper.selectById(userId);
        BeanUtils.copyProperties(user,profileVO);
        // 用户文章数
        int count = bmsTopicMapper.selectCount(new LambdaQueryWrapper<BmsTopic>().eq(BmsTopic::getUserId, userId));
        profileVO.setTopicCount(count);

        // 粉丝数
        int followers = bmsFollowMapper.selectCount((new LambdaQueryWrapper<BmsFollow>().eq(BmsFollow::getParentId, userId)));
        profileVO.setFollowerCount(followers);
        return profileVO;
    }
}
