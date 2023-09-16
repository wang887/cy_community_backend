package com.wcy.handler;

import com.alibaba.fastjson.JSON;
import com.wcy.common.api.ApiErrorCode;
import com.wcy.common.api.ApiResult;
import com.wcy.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ApiResult apiResult = new ApiResult(ApiErrorCode.FailedAUTHORIZED);
        String json = JSON.toJSONString(apiResult);
        WebUtils.renderString(response,json);
    }
}
