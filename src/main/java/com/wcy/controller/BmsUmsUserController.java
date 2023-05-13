package com.wcy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wcy.common.api.ApiResult;
import com.wcy.model.dto.LoginDTO;
import com.wcy.model.dto.RegisterDTO;
import com.wcy.model.entity.BmsTopic;
import com.wcy.model.entity.UmsUser;
import com.wcy.service.EmailService;
import com.wcy.service.IBmsTopicService;
import com.wcy.service.IBmsUmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wcy.jwt.JwtUtil.USER_NAME;

@RestController  //可以自动的转成JSON文件
@RequestMapping("/ums/user")
public class BmsUmsUserController extends BaseController {

    @Resource
    private IBmsUmsUserService iBmsUmsUserService;


    @Resource
    private IBmsTopicService iBmsTopicService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ApiResult<Map<String, Object>> register(@RequestBody RegisterDTO registerDTO) throws Exception {
        UmsUser umsUser = iBmsUmsUserService.executeRegister(registerDTO);
        if (ObjectUtils.isEmpty(umsUser)) {
            return ApiResult.failed("账号注册失败");
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", umsUser);
        return ApiResult.success(map);
    }

    @PostMapping("/resetPwd")
    public ApiResult<String> resetPwd(@RequestBody RegisterDTO registerDTO){
        Boolean aBoolean = iBmsUmsUserService.resetPwd(registerDTO);
        if(aBoolean==false){
            return ApiResult.failed("没有此邮箱");
        }else{
            return ApiResult.success("success");
        }
    }


    @GetMapping("/submitResetPwd")
    @ResponseBody
    public String submitRestPwd(@RequestParam("code") String code){
        System.out.println(code);
        Boolean aBoolean = iBmsUmsUserService.submitRestPwd(code);
        return aBoolean?"重置成功":"重置失败";
    }
    @GetMapping("/active")
    @ResponseBody
    public String active(@RequestParam("code") String code){
        System.out.println(code);
        Boolean aBoolean = iBmsUmsUserService.activeUser(code);
        return aBoolean?"激活成功":"激活失败";
    }


    /**
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    public ApiResult<Map<String,String>> login(@RequestBody LoginDTO loginDTO) throws Exception {
        String[] res = iBmsUmsUserService.execuLogin(loginDTO);
        String token = res[0];
        if(ObjectUtils.isEmpty(token)){
            return ApiResult.failed(res[1]);
        }
        Map<String,String> map = new HashMap<>(16);
        map.put("token",token);
        return ApiResult.success(map);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ApiResult<UmsUser> getUser(@RequestHeader(value = USER_NAME,required = false) String userName) {
        System.out.println(userName);
        UmsUser user = iBmsUmsUserService.getUserByUsername(userName);
        return ApiResult.success(user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ApiResult<Object> logOut() {
        return ApiResult.success(null, "注销成功");
    }

    @GetMapping("/{username}")
    public ApiResult<Map<String, Object>> getUserByName(@PathVariable("username") String username,
                                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> map = new HashMap<>(16);
        UmsUser user = iBmsUmsUserService.getUserByUsername(username);
        Assert.notNull(user, "用户不存在");
        Page<BmsTopic> page = iBmsTopicService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<BmsTopic>().eq(BmsTopic::getUserId, user.getId()));
        map.put("user", user);
        map.put("topics", page);
        return ApiResult.success(map);
    }
    @PostMapping("/update")
    public ApiResult<UmsUser> updateUser(@RequestBody UmsUser umsUser) {
        iBmsUmsUserService.updateById(umsUser);
        return ApiResult.success(umsUser);
    }
}
