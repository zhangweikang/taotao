package com.taotao.store.order.service;

import java.util.Date;



import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.store.order.dao.IOrder;
import com.taotao.store.order.pojo.Order;
import com.taotao.store.order.pojo.PageResult;
import com.taotao.store.order.pojo.ResultMsg;
import com.taotao.store.order.util.ValidateUtil;

@Service
public class OrderService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private IOrder orderDao;

    public TaotaoResult createOrder(String json) {
        Order order = null;
        try {
            order = objectMapper.readValue(json, Order.class);
            // 校验Order对象
            ValidateUtil.validate(order);
        } catch (Exception e) {
            return TaotaoResult.build(400, "请求参数有误!");
        }

        try {
            // 生成订单ID，规则为：userid+当前时间戳
            String orderId = order.getUserId() + "" + System.currentTimeMillis();
            order.setOrderId(orderId);

            // 设置订单的初始状态为未付款
            order.setStatus(1);

            // 设置订单的创建时间
            order.setCreateTime(new Date());
            order.setUpdateTime(order.getCreateTime());

            // 设置买家评价状态，初始为未评价
            order.setBuyerRate(0);

            // 持久化order对象
            orderDao.createOrder(order);
            return TaotaoResult.ok(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.build(400, "保存订单失败!");
    }

    public Order queryOrderById(String orderId) {
        Order order = orderDao.queryOrderById(orderId);
        return order;
    }

    public PageResult<Order> queryOrderByUserNameAndPage(String buyerNick, int page, int count) {
        return orderDao.queryOrderByUserNameAndPage(buyerNick, page, count);
    }

    public ResultMsg changeOrderStatus(String json) {
        Order order = null;
        try {
            order = objectMapper.readValue(json, Order.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultMsg("400", "请求参数有误!");
        }
        return this.orderDao.changeOrderStatus(order);
    }
    //根据订单id删除订单
	public TaotaoResult deleteOrderById(Long orderId) {
		Order order = this.queryOrderById(String.valueOf(orderId));
		if(null == order){
			return TaotaoResult.build(400, "请输入正确的id");
		}
		try {
			orderDao.deleteOrderById(orderId);
			return TaotaoResult.build(200, "OK");
		} catch (Exception e) {
			LOGGER.error("根据订单id删除订单失败", e);
			return TaotaoResult.build(201, "NO");
		}
		
	}
	//查询订单根据商品名、商品Id、订单id、时间、状态、用户id
	public PageResult<Order> queryOrders(String itemName, String itemId,
			String orderId, Date updateTime, Integer status, String userId, Integer page, Integer count) {
		try {
			
			PageResult<Order> paegResult = orderDao.queryOrders(itemName,itemId,orderId,updateTime,status,userId,page,count);
			
			
			return paegResult;
		} catch (Exception e) {
			LOGGER.error("查询订单根据商品名、商品Id、订单id、时间、状态、用户id失败", e);
			return null;
		}
	}
	//根据一些订单id删除订单
	public TaotaoResult deleteOrderByIds(Long[] orderIDS) {
		try {
			orderDao.deleteOrderByIds(orderIDS);
			return TaotaoResult.build(200, "OK");
		} catch (Exception e) {
			LOGGER.error("根据订单ids删除订单失败", e);
			return TaotaoResult.build(201, "NO");
		}
	}

}
