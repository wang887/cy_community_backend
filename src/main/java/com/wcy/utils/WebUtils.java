package com.wcy.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils {
    /**
     * 对前端进行渲染
     * @param response
     * @param message
     * @return
     */
    public static String renderString(HttpServletResponse response, String message) {
        try {
            response.setStatus(200);
            response.setContentType(("application/json"));
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
