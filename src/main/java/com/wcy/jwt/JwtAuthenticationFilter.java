package com.wcy.jwt;

import com.alibaba.fastjson.JSON;
import com.wcy.model.vo.LoginUser;
import com.wcy.service.RedisService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final PathMatcher pathMatcher = new AntPathMatcher();


    @Autowired
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {  //必须重写

//        try {
//            if(isProtectedUrl(request)) {
////                System.out.println(request.getMethod());
//                if(!request.getMethod().equals("OPTIONS"))   //预检
//                    request = JwtUtil.validateTokenAndAddUserIdToHeader(request);
//            }
//        } catch (Exception e) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
//            return;
//        }
//        filterChain.doFilter(request, response);   //相当于放行

        //Security + JWT
        //获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
//        解析token
        String userName;
        try {
            Map<String, Object> map = JwtUtil.paraseToken(token);
            userName = (String) map.get("userName");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        request = JwtUtil.validateTokenAndAddUserIdToHeader(request);   //**
        //从redis中获取用户信息
        String redisKey = "login:"+userName;
        LoginUser loginUser = JSON.parseObject((String) redisService.get(redisKey),LoginUser.class);
        if(Objects.isNull(loginUser)) {
            throw new RuntimeException("用户还未登录");
        }

        //存入SecurityContextHolder

        // TODO 获取权限信息封装到Authentication中

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request,response);

    }

//    private boolean isProtectedUrl(HttpServletRequest request) {  //受保护的url：登陆过后的url
//        List<String> protectedPaths = new ArrayList<String>();
//        protectedPaths.add("/ums/user/info");
//        protectedPaths.add("/ums/user/update");
//        protectedPaths.add("/post/create");
//        protectedPaths.add("/post/update");
//        protectedPaths.add("/post/delete/*");
//        protectedPaths.add("/comment/add_comment");
//        protectedPaths.add("/relationship/subscribe/*");
//        protectedPaths.add("/relationship/unsubscribe/*");
//        protectedPaths.add("/relationship/validate/*");
//        protectedPaths.add("/question/create");
//        protectedPaths.add("/question/follower");
////        protectedPaths.add("/question/detail");
//
//        boolean bFind = false;
//        for( String passedPath : protectedPaths ) {
//            bFind = pathMatcher.match(passedPath, request.getServletPath());
//            if( bFind ) {
//                break;
//            }
//        }
//        return bFind;
//    }

}