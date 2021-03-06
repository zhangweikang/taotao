package com.taotao.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.service.CartCookieService;
import com.taotao.web.service.CartService;
import com.taotao.web.threadlocal.UserThreadLocal;
import com.taotao.web.vo.Item;
import com.taotao.web.vo.ItemCart;
import com.taotao.web.vo.User;

@RequestMapping("cart")
@Controller
public class CartController {

    @Autowired
    private CartCookieService cartCookieService;

    @Autowired
    private CartService cartService;

    /**
     * 添加商品到购物车
     * 
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("add/{itemId}/{num}")
    public String addItemToCart(@PathVariable("itemId") Long itemId, HttpServletRequest request,
            HttpServletResponse response,@PathVariable("num") Integer num) {
        // 判断用户是否登录，如果未登录，将数据保存到cookie中，如果已登录，保存到数据库中
        User user = UserThreadLocal.get();
        if (null == user) {
            // 未登录
            // 数据保存到cookie中
            this.cartCookieService.addItem(itemId, num, request, response);
        } else {
            // 登录
            this.cartService.addItem(user, itemId, num);
        }

        return "redirect:/cart/show.html";
    }

    @RequestMapping("show")
    public ModelAndView showCart(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("cart");
        User user = UserThreadLocal.get();
        List<ItemCart> itemCarts = null;
        if (user == null) {
            itemCarts = this.cartCookieService.queryCartList(request);
        } else {
            // 查询数据库
            itemCarts = this.cartService.queryCartList(user);
        }
        mv.addObject("cartList", itemCarts);
        return mv;
    }

    /**
     * 更改购物车中的商品数量
     * 
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateNum(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num,
            HttpServletRequest request, HttpServletResponse response) {
        User user = UserThreadLocal.get();
        int number=0;
        if (user == null) {
            number = this.cartCookieService.updateNum(itemId, num, request, response);
        } else {
            // 查询数据库
            this.cartService.updateNum(user, itemId, num);
        }
        return TaotaoResult.ok(number);
    }

    /**
     * 从购物车中删除商品数据
     * 
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("delete/{itemId}")
    public String delete(@PathVariable("itemId") Long itemId, HttpServletRequest request,
            HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (user == null) {
            this.cartCookieService.delete(itemId, request, response);
        } else {
            // 查询数据库
            this.cartService.delete(user, itemId);
        }
        return "redirect:/cart/show.html";
    }
    
    @RequestMapping("deleteChecked")
    public String deleteCheckedCart(@RequestParam("ids")String ids,HttpServletRequest request, HttpServletResponse response){
    	 String[] split = StringUtils.split(ids, ":");
    	 User user = UserThreadLocal.get();
         //将字符串解析为id集合
         List<Long> idss = new ArrayList<Long>();
         for (String id : split) {
         	idss.add(Long.valueOf(id));
         }
         if(!idss.isEmpty()){
             if(user==null){
                 cartCookieService.deleteIds(split, request, response);
             }else{
        	 cartService.deleteItems(user, idss);
             }
         }
         return "redirect:/cart/show.html";
    }

}
