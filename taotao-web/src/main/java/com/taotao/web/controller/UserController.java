package com.taotao.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.service.CartService;
import com.taotao.web.service.UserService;

import com.taotao.web.vo.User;

import com.taotao.web.vo.ItemCart;
import com.taotao.web.vo.User;
import com.taotao.web.vo.UserVo;


@RequestMapping("user")
@Controller
public class UserController {

    public static final String TICKET = "TT_TICKET";
    public static final String CHECKCODE = "1234";

    public static final String CART = "TT_CART";

    public static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private CartService cartService;

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
        itemCartMerger(request, ticket);

        return TaotaoResult.ok();
    }

    
    
    /**
     * 跳转到忘记密码页面
     * 
     * @return
     */
    @RequestMapping("forgetPassword")
    public String findPassword(){
        return "forgetPassword";
    }

    /**
     * 跳转到发送验证码页面
     * @param phone
     * @return
     */
    
    @RequestMapping(value="confirmInfo",method=RequestMethod.GET)
    public ModelAndView confirmInfo(@RequestParam("phone") String phone){
        ModelAndView mv = new ModelAndView();
        User user = userService.queryUserByPhone(phone);
        //System.out.println(user);
        if(user == null){
            mv.setViewName("forgetPassword");
            return mv;
            }
        mv.addObject("user", user);
        mv.setViewName("confirmInfo");
        return mv;
    }
    
    
    /**
     * 发送校验码
     * 
     */
    
    @RequestMapping(value="sendCode",method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult sendCode(@RequestParam("phone") String phone){
        //String checkCode = "1234";
        userService.sendCode(phone,CHECKCODE);
        
        return null;
        
    }
    
    /**
     * 实现验证码的校验
     * 
     */
    @RequestMapping(value="validateCode",method=RequestMethod.GET)
    public ModelAndView validateCode(@RequestParam("phone") String phone,@RequestParam("code")String code){
        ModelAndView mv = new ModelAndView();
        User user = userService.queryUserByPhone(phone);
        if(CHECKCODE.equals(code) ){
           mv.addObject("user",user);
           mv.setViewName("changePassword");
           return mv;
        }
        mv.setViewName("confirmInfo");
        return mv;
    }
    
    /**
     * 
     * 实现密码的更改
     */
    @RequestMapping(value="updatePassword",method=RequestMethod.POST)
    public ModelAndView updatePassword(@RequestParam("password")String password,@RequestParam("id")String id){
        userService.updatePassword(password,id);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("NewPassword");
        return mv;
    }

    
    /**
     * 登陆后修改个人资料
     * 
     * @param request
     */
    @RequestMapping(value = "userinfo/update", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateUserInfo(UserVo userVo, HttpServletRequest request) {

        String ticket = CookieUtils.getCookieValue(request, UserController.TICKET);
        User user = this.userService.queryUserByTicket(ticket);

        userVo.setUserId(user.getId());

        return this.userService.updateUserInfo(userVo);
    }

    /**
     * 登陆后查询个人资料
     * 
     * @param request
     */
    @RequestMapping("userinfo/query")
    public ModelAndView UserInfo(HttpServletRequest request) {

        ModelAndView mv = new ModelAndView();
        String ticket = CookieUtils.getCookieValue(request, UserController.TICKET);
        User user = this.userService.queryUserByTicket(ticket);

        UserVo userVo = this.userService.queryInfoByUserId(user.getId());

        mv.addObject("userVo", userVo);
        mv.setViewName("my-info");
        return mv;
    }

    /**
     * 登陆后合并购物车数据
     * 
     * @param request
     * @param ticket
     */
    private void itemCartMerger(HttpServletRequest request, String ticket) {
        // 获取cookie中购物车标识,查询redis
        String tt_cart = CookieUtils.getCookieValue(request, CART);
        if (null != tt_cart) {
            Map<String, String> r_cart = redisService.hgetAll(1, tt_cart);
            if (!r_cart.isEmpty()) {
                List<ItemCart> r_itemcarts = new ArrayList<ItemCart>();
                Set<Entry<String, String>> entrySet = r_cart.entrySet();
                for (Entry<String, String> entry : entrySet) {
                    String value = entry.getValue();
                    try {
                        ItemCart itemCart = MAPPER.readValue(value, ItemCart.class);
                        r_itemcarts.add(itemCart);
                    } catch (Exception e) {
                        LOGGER.error("cookie购物车数据获取失败,商品id为:" + entry.getKey(), e);
                    }
                }

                // 获取登录用户
                User user = this.userService.queryUserByTicket(ticket);
                // 根据用户id查询购物车数据
                String jsonData = null;
                try {
                    jsonData = apiService.doGet("http://manage.taotao.com/rest/cart/query/" + user.getId());
                } catch (Exception e) {
                    // 重复三次请求
                    LOGGER.error("请求购物车数据失败,准备再次请求");
                    for (int i = 1; i <= 3; i++) {
                        try {
                            Thread.sleep(200);
                            jsonData = apiService.doGet("http://manage.taotao.com/rest/cart/query/"
                                    + user.getId());
                            break;
                        } catch (Exception e1) {
                            LOGGER.error("请求购物车数据失败,准备再次请求");
                        }
                    }
                }
                List<ItemCart> itemCarts = null;
                if (StringUtils.isNoneBlank(jsonData)) {
                    try {
                        itemCarts = MAPPER.readValue(jsonData, MAPPER.getTypeFactory()
                                .constructCollectionType(List.class, ItemCart.class));
                    } catch (Exception e) {
                        LOGGER.error("解析后台的购物车数据出错!", e);
                    }
                    if (null == itemCarts) {
                        itemCarts = new ArrayList<ItemCart>();
                    }
                }
                // 合并cookie和后台数据
                if (null != r_itemcarts && null != itemCarts) {
                    for (ItemCart r_itemCart : r_itemcarts) {
                        for (ItemCart itemCart : itemCarts) {
                            // 数据库中商品存在,更改商品数量
                            if (r_itemCart.getId() == itemCart.getId()) {
                                cartService.updateNum(user, r_itemCart.getId(), r_itemCart.getNum()
                                        + itemCart.getNum());
                                r_itemcarts.remove(r_itemCart);
                            }
                        }
                    }
                }
                if (null != r_itemcarts) {
                    for (ItemCart itemCart : r_itemcarts) {
                        cartService.addItem(user, itemCart.getId(), itemCart.getNum());
                    }
                }
                // 清除cookie相关数据
                redisService.del(1, tt_cart);
            }
        }
    }


}
