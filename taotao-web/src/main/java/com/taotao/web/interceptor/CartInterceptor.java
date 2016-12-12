package com.taotao.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.util.CookieUtils;
import com.taotao.web.controller.UserController;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;
import com.taotao.web.vo.User;

public class CartInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 获取cookie中的ticket
        String ticket = CookieUtils.getCookieValue(request, UserController.TICKET);
        // 校验ticket是否有效，如果无效，跳转到登录页面
        if (StringUtils.isBlank(ticket)) {
            // 页面跳转
            UserThreadLocal.set(null);
            return true;
        }

        // 需要到sso系统中查询
        User user = this.userService.queryUserByTicket(ticket);
        if(null == user){
            // 用户登录状态已经失效
            UserThreadLocal.set(null);
            return true;
        }
        //将user对象放置到线程中
        UserThreadLocal.set(user);
        // 用户登录状态有效
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {

    }

}
