package com.open.web.interceptor;

import com.open.web.bean.User;
import com.open.web.config.ConfigMap;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @auther 程佳伟
 * @create 2019-10-12 23:30
 */
@Log4j2
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();
        log.debug("LoginInterceptor 拦截请求 {} ( params [ {} ] ) ",uri,request.getParameterMap());

        User user = (User) request.getSession().getAttribute("session_user");
        if(user == null){	//未登陆

            if((request.getRequestURI()).startsWith("/toPage")){
                if(ConfigMap.getWhite().contains(request.getParameter("pageName"))){
                    log.info("request url {} 被LoginInterceptor放行",uri);
                    return true;
                }
            }

            response.sendRedirect("/api/v2/user/toLogin");
            log.info("request url {} 被LoginInterceptor拦截",uri);
            return false;
        }

        log.info("request url {} 被LoginInterceptor放行",uri);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
