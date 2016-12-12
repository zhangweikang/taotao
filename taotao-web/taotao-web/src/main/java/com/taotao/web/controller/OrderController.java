package com.taotao.web.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;
import com.taotao.web.vo.Item;
import com.taotao.web.vo.ItemCart;
import com.taotao.web.vo.Order;
import com.taotao.web.vo.User;

@RequestMapping("order")
@Controller
public class OrderController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private CartService cartService;

    /**
     * 跳转到订单确认页
     * 
     * @param itemId
     * @return
     */
    @RequestMapping("add/{itemId}")
    public ModelAndView addOrder(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("order");
        Item item = this.itemService.getItemById(itemId);
        mv.addObject("item", item);
        return mv;
    }
    
    @RequestMapping("create")
    public ModelAndView createOrder() {
        ModelAndView mv = new ModelAndView("order-cart");
        User user = UserThreadLocal.get();
        List<ItemCart> itemCarts = this.cartService.queryCartList(user);
        mv.addObject("carts", itemCarts);
        return mv;
    }

    /**
     * 提交订单
     * 
     * @param order
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult submit(Order order) {
        // 从ThreadLocal中查询用户信息,将用户id和用户名填入Order对象中
        User user = UserThreadLocal.get();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());

        String orderNumber = this.orderService.submitOrder(order);
        if (orderNumber == null) {
            return TaotaoResult.build(201, "创建订单失败!");
        }
        return TaotaoResult.ok(orderNumber);
    }

    /**
     * 下单成功页面
     * 
     * @param orderId
     * @return
     */
    @RequestMapping("success")
    public ModelAndView success(@RequestParam("id") Long orderId) {
        ModelAndView mv = new ModelAndView("success");
        Order order = this.orderService.queryOrderById(orderId);
        mv.addObject("order", order);
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }

}
