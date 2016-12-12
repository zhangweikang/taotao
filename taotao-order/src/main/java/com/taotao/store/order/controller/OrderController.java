package com.taotao.store.order.controller;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.joda.JodaDateTimeFormatAnnotationFormatterFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.store.order.pojo.Order;
import com.taotao.store.order.pojo.PageResult;
import com.taotao.store.order.pojo.ResultMsg;
import com.taotao.store.order.service.OrderService;

@RequestMapping("/order")
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * 
     * @param json
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public TaotaoResult createOrder(@RequestBody String json) {
        return orderService.createOrder(json);
    }

    /**
     * 根据订单ID查询订单
     * 
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/query/{orderId}", method = RequestMethod.GET)
    public Order queryOrderById(@PathVariable("orderId") String orderId) {
        return orderService.queryOrderById(orderId);
    }

    /**
     * 根据用户名分页查询订单
     * 
     * @param buyerNick
     * @param page
     * @param count
     * @return
     */
    @ResponseBody
    @RequestMapping("/query/{buyerNick}/{page}/{count}")
    public PageResult<Order> queryOrderByUserNameAndPage(@PathVariable("buyerNick") String buyerNick,
            @PathVariable("page") Integer page, @PathVariable("count") Integer count) {
        return orderService.queryOrderByUserNameAndPage(buyerNick, page, count);
    }

    /**
     * 修改订单状态
     * 
     * @param json
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeOrderStatus", method = RequestMethod.POST)
    public ResultMsg changeOrderStatus(@RequestBody String json) {
        return orderService.changeOrderStatus(json);
    }
    /**
     * 根据id删除订单
     * 
     * @param json
     */
    @RequestMapping(value="/deleteOrderById/{orderIds}",method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult deleteOrderById(@PathVariable("orderIds")String orderIds){
    	
    	String[] ids = orderIds.split(",");    	
    	if(ids.length==1){    		
    		Long orderId = Long.valueOf(ids[0].toString());
    		return orderService.deleteOrderById(orderId);
    	}
    	else{
    		Long[] orderIDS = new Long[ids.length];
    		for(int i=0; i<ids.length; i++){
    			orderIDS[i] = Long.valueOf(ids[i]);
    		}
    		return orderService.deleteOrderByIds(orderIDS);
    	}
    	
    }
    /**
     * 根据 商品名称|商品编号|订单编号
     * |时间(最近三个月 3,最近两个月2,最近一个月 1)
     * |状态(1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭)查询订单
     * |page
     * |count
     */
     @RequestMapping(value="/queryOrders",method = RequestMethod.POST)
     @ResponseBody
     public PageResult<Order> queryOrders(@RequestParam(value="itemName",required=false)String itemName,
    		 @RequestParam(value="itemId",required=false)String itemId,
    		 @RequestParam(value="orderId",required=false)String orderId,
    		 @RequestParam(value="updateTime",required=false,defaultValue="3")Integer updateTimeL,
    		 @RequestParam(value="status",required=false)Integer status,
    		 @RequestParam("userId")String userId,
    		 @RequestParam(value="page",defaultValue="0")Integer page,
    		 @RequestParam(value="count",defaultValue="10")Integer count){
    	 Date updateTime = null;
    	 DateTime dateTime = new DateTime(System.currentTimeMillis());
    	 if(updateTimeL>3 || updateTimeL<1){
    		 return null;
    	 }
    	 if(updateTimeL==1){
    		
    		 
    		 updateTime = dateTime.minusMonths(1).toDate();
    	 }
         if(updateTimeL==2){
        	
    		 updateTime = dateTime.minusMonths(2).toDate();
    	 }
         if(updateTimeL==3){
        	
    		 updateTime = dateTime.minusMonths(3).toDate();
    	 }
         
    	 return orderService.queryOrders(itemName,itemId,orderId,updateTime,status,userId,page,count);
     }
}
