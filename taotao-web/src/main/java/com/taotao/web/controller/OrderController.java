package com.taotao.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.service.ApiService;
import com.taotao.common.vo.EasyUIResult;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.MyOrderSercice;
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
    @Autowired
    private ApiService apiService;
    @Autowired
    private MyOrderSercice myOrderService;
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
    public ModelAndView createOrder(@RequestParam("ids")String ids) {
        ModelAndView mv = new ModelAndView("order-cart");
        String[] split = StringUtils.split(ids, ":");
        //将字符串解析为id集合
        List<Long> idss = new ArrayList<Long>();
        for (String id : split) {
                idss.add(Long.valueOf(id));
        }
        List<ItemCart> itemCarts = new ArrayList<ItemCart>();
        //获取数据库中该用户购物的所有商品,选取已选择的商品
        if(!idss.isEmpty()){
                User user = UserThreadLocal.get();
                List<ItemCart> allItemCarts = this.cartService.queryCartList(user);
                for (ItemCart itemCart : allItemCarts) {
                        for (Long id : idss) {
                                if((long)itemCart.getId() == (long)id){
                                        itemCarts.add(itemCart);
                                }
                        }
                        }
                
                mv.addObject("carts", itemCarts);
                //删除已选择的商品
                cartService.deleteItems(user, idss);
        }
        
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
    
    
    @RequestMapping(value="queryOrder/{orderId}",method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView queryOrderById(@PathVariable("orderId")Long orderId){
        ModelAndView mv = new ModelAndView("my-order");
        //查询需要的数据
        Order order = this.orderService.queryOrderById(orderId);
        //将查到的数据加入到mv中
        mv.addObject(order);
        return mv;
    }
    
    //分页查询订单
    
    @RequestMapping(value="queryOrdersByUser")
    @ResponseBody
    public ModelAndView queryOrdersByUser(@RequestParam(value="page",defaultValue="0")Long page,@RequestParam(value="count",defaultValue="20")Long count){
        ModelAndView mv = new ModelAndView("my-orders");
        User user = UserThreadLocal.get();
        EasyUIResult easyUIResult = new EasyUIResult();
        if(user != null){
           easyUIResult =  this.orderService.queryOrdersByUserId(user.getId(),page,count);
           if(easyUIResult != null){
               mv.addObject(easyUIResult);
           }
            
        }
        return mv;
    }
    
    @RequestMapping(value="deleteOrderByOrderId/{orderId}",method=RequestMethod.GET)
    @ResponseBody
    public String deltetOrderByOrderId(@PathVariable("orderId")String orderId,HttpServletResponse response){
        String url = myOrderService.MYORDER_DELETE_URL+orderId;
        String jsonDate;
        try {
            jsonDate = apiService.doGet(url);
            TaotaoResult taotaoResult = TaotaoResult.formatToList(jsonDate, TaotaoResult.class);
            if (taotaoResult.getStatus()==200) {
                response.sendRedirect("/order/queryOrdersByUser.html");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
