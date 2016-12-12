package com.taotao.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.util.CookieUtils;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.service.UserService;

@RequestMapping("user")
@Controller
public class UserController {

    public static final String TICKET = "TT_TICKET";

    @Autowired
    private UserService userService;

    /**
     * 跳转到注册页面
     * 
     * @return
     */
    @RequestMapping("register")
    public String register() {
        return "register";
    }

    /**
     * 跳转到登录页面
     * 
     * @return
     */
    @RequestMapping("login")
    public String login() {
        return "login";
    }

    /**
     * 实现用户注册功能
     * 
     * @param username
     * @param phone
     * @param password
     * @return
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult doRegister(@RequestParam("username") String username,
            @RequestParam("phone") String phone, @RequestParam("password") String password) {
        return this.userService.register(username, password, phone);
    }

    /**
     * 实现用户登录功能
     * 
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult doLogin(@RequestParam("username") String username,
            @RequestParam("password") String password, HttpServletRequest request,
            HttpServletResponse response) {
        // 调用SSO接口，实现登录
        String ticket = this.userService.login(username, password);
        if (ticket == null) {
            // 登录失败
            return TaotaoResult.build(201, "用户名或密码错误!");
        }

        // 登录成功，拿到ticket，将ticket写入到cookie中
        // cookie的有效时间：session会话级别
        CookieUtils.setCookie(request, response, TICKET, ticket);
        return TaotaoResult.ok();
    }
}
