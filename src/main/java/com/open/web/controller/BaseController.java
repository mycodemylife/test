package com.open.web.controller;

import com.open.web.utils.QueryData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description
 * @auther 程佳伟
 * @create 2019-10-12 23:45
 */
@Controller
public class BaseController {

    /**
     * 简单的跳转到指定的叶脉呢
     * @param pageName 页面的名称
     * @return
     */
    @RequestMapping("/toPage")
    public String toPage(String pageName){

        return pageName;
    }

    /**
     * 创建指定的 ModelAndView
     * @param url 跳转的页面
     * @return
     */
    public ModelAndView getModelAndView(String url,String code,String message) {

        ModelAndView mv = new ModelAndView();

        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
        Map<String, String[]> parameterMap = request.getParameterMap();

        mv.addAllObjects(parameterMap);
        mv.addObject("uri", request.getRequestURI());
        mv.addObject("code", code);
        mv.addObject("message", message);
        mv.setViewName(url);
        return mv;
    }

    /**
     * 得到request对象
     */
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        return request;
    }

    public QueryData getQueryData() {
        return new QueryData(this.getRequest());
    }
}
