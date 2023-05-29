package com.wcy.interceptor;

import com.alibaba.fastjson.JSON;
import com.wcy.annotation.AccessLimit;
import com.wcy.common.api.ApiErrorCode;
import com.wcy.common.exception.ApiException;
import com.wcy.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 接口防刷接口处理
 */
@Slf4j
public class AccessLimitInterceptor implements HandlerInterceptor {


    @Autowired
    private RedisService redisService;


    /**
     * 锁住时的key前缀
     */
    public static final String LOCK_PREFIX = "LOCK";

    /**
     * 统计次数时的key前缀
     */
    public static final String COUNT_PREFIX = "COUNT";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod targetMethod = (HandlerMethod) handler;

            AccessLimit classAnnotation = targetMethod.getMethod().getDeclaringClass().getAnnotation(AccessLimit.class);
            boolean isBrushForAllInterface = false;
            String ip = request.getRemoteAddr();
            String uri = request.getRequestURI();
            long second = 1L;
            long maxTime = 3L;
            long forbiddenTime = 1L;
            if(!Objects.isNull(classAnnotation)){
                log.info("目标接口方法所在类上有@AccessLimit注解");
                isBrushForAllInterface = true;
                second = classAnnotation.second();
                maxTime = classAnnotation.maxTime();
                forbiddenTime = classAnnotation.forbiddenTime();
            }
            AccessLimit methodAnnotation = targetMethod.getMethodAnnotation(AccessLimit.class);
            if(!Objects.isNull(methodAnnotation)){
                second = methodAnnotation.second();
                maxTime = methodAnnotation.maxTime();
                forbiddenTime = methodAnnotation.forbiddenTime();
                //判断这个接口这个接口是否已经被禁用
                if(isForbidden(second,maxTime,forbiddenTime,ip,uri)){
                    throw new ApiException(ApiErrorCode.ACCESS_FREQUENT);
                }
            }else{
                // 目标接口方法处无@AccessLimit注解，但还要看看其类上是否加了（类上有加，代表针对此类下所有接口方法都要进行防刷处理）
                if(isForbidden(second,maxTime,forbiddenTime,ip,uri) && isBrushForAllInterface){
                    throw new ApiException(ApiErrorCode.ACCESS_FREQUENT);
                }
            }

        }

        return true;
    }


    private boolean isForbidden(long second, long maxTime, long forbiddenTime, String ip, String uri){
        String lockKey = LOCK_PREFIX + ip + uri;

        Object isLock = redisService.get(lockKey);
        //判断此ip用户访问此接口是否已经被禁用
        if (Objects.isNull(isLock)){
            String countkey = COUNT_PREFIX+ip+uri;
            String count = (String) redisService.get(countkey);
            if(Objects.isNull(count)){
                log.info("首次访问");
                redisService.set(countkey, JSON.toJSONString(1),second);
            }else{

                if(Integer.parseInt(count)<maxTime){
                    redisService.increment(countkey);
                }else {
                    log.info("{}禁用访问{}", ip, uri);
                    //禁用
                    redisService.set(lockKey, JSON.toJSONString(1), forbiddenTime);
                    redisService.del(countkey);
                    return true;
                }
            }
        }else{
            // 此用户访问此接口已被禁用
            return true;
        }
        return false;
    }
}
